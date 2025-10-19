package com.loctran.s3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class S3ServicesTest {

    private S3Services underTest;

    @Mock
    private S3Client s3Client;

    @BeforeEach
    void setUp() {
        underTest = new S3Services(s3Client);
    }

    @Test
    void canPutObject() throws IOException {
        String bucket = "car";
        String key = "4121213";
        byte[] content = "test".getBytes();

        underTest.putObject(bucket, key, content);

        ArgumentCaptor<PutObjectRequest> putObjectRequestArgumentCaptor = ArgumentCaptor.forClass(PutObjectRequest.class);
        ArgumentCaptor<RequestBody> requestBodyArgumentCaptor = ArgumentCaptor.forClass(RequestBody.class);

        verify(s3Client).putObject(putObjectRequestArgumentCaptor.capture(), requestBodyArgumentCaptor.capture());

        assertThat(bucket).isEqualTo(putObjectRequestArgumentCaptor.getValue().bucket());
        assertThat(key).isEqualTo(putObjectRequestArgumentCaptor.getValue().key());

        RequestBody requestBody = requestBodyArgumentCaptor.getValue();

        assertThat(requestBody.contentStreamProvider().newStream().readAllBytes()).isEqualTo(content);
    }

    @Test
    void getObject() throws IOException {
        String bucket = "car";
        String key = "4121213";
        byte[] content = "test".getBytes();

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        ResponseInputStream<GetObjectResponse> res = mock(ResponseInputStream.class);

        when(s3Client.getObject(getObjectRequest)).thenReturn(res);
        when(res.readAllBytes()).thenReturn(content);

        byte[] expected = underTest.getObject(bucket, key);

        assertThat(expected).isEqualTo(content);
    }

    @Test
    void willThrowAnExceptionWhenGetObject() throws IOException {
        String bucket = "car";
        String key = "4121213";

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        ResponseInputStream<GetObjectResponse> res = mock(ResponseInputStream.class);

        when(s3Client.getObject(getObjectRequest)).thenReturn(res);
        when(res.readAllBytes()).thenThrow(IOException.class);

        assertThatThrownBy(() -> underTest.getObject(bucket, key))
                .isInstanceOf(RuntimeException.class)
                .hasRootCauseInstanceOf(IOException.class);
    }
}