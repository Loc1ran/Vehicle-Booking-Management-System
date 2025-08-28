const UserProfile = ({name, age, gender, ... props}) => {
    return (
        <div>
            <h1>{name}</h1>
            <p>{age}</p>
            <img src={`https://randomuser.me/api/portraits/men/75.jpg`}/>
            <p>{props.children}</p>
        </div>
    )
}

export default UserProfile;