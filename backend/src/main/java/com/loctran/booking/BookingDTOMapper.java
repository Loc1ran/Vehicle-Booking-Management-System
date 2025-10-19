package com.loctran.booking;

import com.loctran.user.UserDTOMapper;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class BookingDTOMapper implements Function<Booking, BookingDTO> {
    private final UserDTOMapper userDTOMapper;

    public BookingDTOMapper(UserDTOMapper userDTOMapper) {
        this.userDTOMapper = userDTOMapper;
    }

    @Override
    public BookingDTO apply(Booking booking) {
        return new BookingDTO(
                booking.getId(),
                booking.getCars(), userDTOMapper.apply(booking.getUsers())
        );
    }
}
