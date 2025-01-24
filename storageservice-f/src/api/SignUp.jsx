import React, { useState } from "react";
import axios from "axios";

const SERVER_URL = "http://localhost:8081"

const RegistrationComponent = () => {
        const [registerData, setRegisterData] = useState({
                firstName: "",
                lastName: "",
                email: "",
                login: "",
                password: ""
        });

        const [response, setResponse] = useState("");

        const handleRegisterChange = (e) => {
                setRegisterData({
                        ...registerData,
                        [e.target.name]: e.target.value
                });
        };

        const handleRegisterSubmit = async (e) => {
                e.preventDefault();
                try {
                        console.log("Register data before sending:", registerData);
                        const res = await axios.post(SERVER_URL + "/api/v1/auth/register", registerData);
                        setResponse(res.data);
                } catch (error) {
                        setResponse(`Error: ${error.response?.data?.message || error.message}`);
                }
        };

        return (
                <div>
                        <h1>Registration</h1>

                        <form onSubmit={handleRegisterSubmit}>
                                <input
                                        type="text"
                                        name="firstName"
                                        placeholder="First Name"
                                        value={registerData.firstName}
                                        onChange={handleRegisterChange}
                                        required
                                />
                                <input
                                        type="text"
                                        name="lastName"
                                        placeholder="Last Name"
                                        value={registerData.lastName}
                                        onChange={handleRegisterChange}
                                        required
                                />
                                <input
                                        type="email"
                                        name="email"
                                        placeholder="Email"
                                        value={registerData.email}
                                        onChange={handleRegisterChange}
                                        required
                                />
                                <input
                                        type="text"
                                        name="login"
                                        placeholder="Login"
                                        value={registerData.login}
                                        onChange={handleRegisterChange}
                                        required
                                />
                                <input
                                        type="password"
                                        name="password"
                                        placeholder="Password"
                                        value={registerData.password}
                                        onChange={handleRegisterChange}
                                        required
                                />
                                <button type="submit">Register</button>
                        </form>

                        <p>{response}</p>
                </div>
        );
};

export default RegistrationComponent;
