import { useState, useEffect } from "react";
import { useLocation } from "react-router-dom";
import axios from "axios";

const SERVER_URL = "http://localhost:8081";

const AdminPage = () => {
  const location = useLocation();
  const searchParams = new URLSearchParams(location.search);
  const key = searchParams.get("key");

  const [response, setResponse] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchData = async () => {
      try {
        const res = await axios.get(SERVER_URL + "/api/v1/auth/confirm", {
          params: { key },
        });
        setResponse(res.data);
      } catch (err) {
        console.error("Ошибка при отправке запроса:", err);
        setError("Ошибка при верификации.");
      }
    };

    if (key) {
      fetchData();
    } else {
      setError("Отсутствует параметр ключа.");
    }
  }, [key]);

  return (
    <div>
      <h1>Account Verification</h1>
      {error ? <p style={{ color: "red" }}>{error}</p> : <p>{response}</p>}
    </div>
  );
};

export default AdminPage;
