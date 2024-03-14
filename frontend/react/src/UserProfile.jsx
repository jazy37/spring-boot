const UserProfile = ({name, gender, imageNumber, ...props}) => {

    gender = gender === "MALE" ? "men" : "women"
    return (
        <div>
            <h2>{ name }</h2>
            <img src={`https://randomuser.me/api/portraits/${gender}/${imageNumber}.jpg`}/>
            {props.children}
        </div>
    )
}

export default UserProfile;