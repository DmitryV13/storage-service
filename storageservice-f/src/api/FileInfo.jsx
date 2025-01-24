import { useNavigate, useLocation } from "react-router-dom";
import React, { useEffect, useState } from "react";
import axios from "axios";
import { useAuth } from "./AuthContext";

const SERVER_URL = "http://localhost:8081";

const FileInfo = () => {
  const { token } = useAuth();
  const location = useLocation();
  const { fileId } = location.state || {};
  const [error, setError] = useState("");
  const [fileInfo, setFileInfo] = useState(null); // Хранение данных файла

  const fetchInfo = async () => {
    try {
      const res = await axios.post(
        SERVER_URL + "/api/v1/profile/info",
        { fileId },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
          withCredentials: true,
        }
      );

      setFileInfo(res.data); // Сохраняем данные из ответа
    } catch (error) {
      setError(`Error fetching profile: ${error.response?.data?.message || error.message}`);
    }
  };

  useEffect(() => {
    if (fileId) {
      fetchInfo();
    } else {
      setError("No file ID provided.");
    }
  }, [fileId]);

  return (
    <div>
      <h1>File Info</h1>
      {error && <p style={{ color: "red" }}>{error}</p>}

      {fileInfo ? (
        <div>
          <p><strong>Name:</strong> {fileInfo.name}</p>
          <p><strong>Size:</strong> {fileInfo.size} bytes</p>
          <p><strong>MIME Type:</strong> {fileInfo.mimeType}</p>
          <p><strong>Path:</strong> {fileInfo.path}</p>
          <p><strong>Creation Date:</strong> {new Date(fileInfo.creationDate).toLocaleString()}</p>
        </div>
      ) : (
        !error && <p>Loading file information...</p>
      )}
    </div>
  );
};

export default FileInfo;
