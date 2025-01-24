import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { useAuth } from "./AuthContext";

const SERVER_URL = "http://localhost:8081";

const AuthenticationComponent = () => {

  const { token, setToken } = useAuth();
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const [loginData, setLoginData] = useState({
    login: "",
    password: ""
  });

  const [response, setResponse] = useState("");
  const [profile, setProfile] = useState(null); // Для хранения данных профиля
  //const [token, setToken] = useState(""); // Для хранения токена

  const handleLoginChange = (e) => {
    setLoginData({
      ...loginData,
      [e.target.name]: e.target.value
    });
  };

  const handleLoginSubmit = async (e) => {
    e.preventDefault();

    if (token) {
      setResponse("You are already authenticated!");
      return;
    }

    try {
      const res = await axios.post(SERVER_URL + "/api/v1/auth/authenticate", loginData);
      const token = res.data.token;
      setToken(token);

      if (res.data.authorities.includes("ADMIN")) {
        navigate("/admin");
      } else if (res.data.authorities.includes("USER")) {
        navigate("/profile");
      } else {
        setError(`Role error: ${res.data.authorities}`);
      }

    } catch (error) {
      setResponse(`Error: ${error.response?.data?.message || error.message}`);
      setToken("");
      localStorage.removeItem("token");
      setProfile(null);
      setError("");
    }
  };

  return (
    <div>
      <h1>Authentication</h1>

      <form onSubmit={handleLoginSubmit}>
        <input
          type="text"
          name="login"
          placeholder="Login"
          value={loginData.login}
          onChange={handleLoginChange}
          required
        />
        <input
          type="password"
          name="password"
          placeholder="Password"
          value={loginData.password}
          onChange={handleLoginChange}
          required
        />
        <button type="submit">Login</button>
      </form>


      {error && <p style={{ color: "red" }}>{error}</p>}
    </div>
  );
};

export default AuthenticationComponent;
