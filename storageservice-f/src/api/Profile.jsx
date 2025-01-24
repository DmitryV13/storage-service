import { useNavigate } from "react-router-dom";
import React, { useEffect, useState } from "react";
import axios from "axios";
import { useAuth } from "./AuthContext";

const SERVER_URL = "http://localhost:8081";

const Profile = () => {
  const { token, setToken } = useAuth();
  const navigate = useNavigate();

  const [profile, setProfile] = useState(null);
  const [error, setError] = useState("");
  const [allocationResp, setResponse] = useState("");
  const [file, setFile] = useState(null);

  const fetchProfile = async () => {
    if (!token) {
      setError("You are not authenticated or token expired!");
      setProfile(null);
      return;
    }

    try {
      const profileRes = await axios.get(SERVER_URL + "/api/v1/profile", {
        headers: {
          Authorization: `Bearer ${token}` // Добавляем токен в заголовок
        },
        withCredentials: true
      });
      setProfile(profileRes.data); // Сохраняем данные профиля
    } catch (error) {
      setError(`Error fetching profile: ${error.response?.data?.message || error.message}`);
      handleLogout()
    }
  };

  useEffect(() => {
    fetchProfile(); // Загружаем данные профиля при монтировании компонента
  }, []);

  const handleLogout = () => {
    setToken("");
    localStorage.removeItem("token");
    setProfile(null);
    setError("");
  };

  const handleFileChange = (e) => {
    setFile(e.target.files[0]); // Сохраняем выбранный файл в состояние
  };

  const handleUpload = async () => {
    if (!file) {
      setError("Please select a file to upload.");
      return;
    }

    const formData = new FormData();
    formData.append("file", file);

    try {
      const res = await axios.post(SERVER_URL + "/api/v1/profile/upload", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
          Authorization: `Bearer ${token}`
        }
      });
      alert("File uploaded successfully: " + res.data);
    } catch (error) {
      setError(`Error uploading file: ${error.response?.data?.message || error.message}`);
    }
  };

  const handleAllocate = async (size) => {
    try {
      const res = await axios.post(SERVER_URL + "/api/v1/profile/allocate", { allocatedSpace: size }, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setResponse(res.data);
    } catch (error) {
      setResponse(`Error: ${error.response?.data?.message || error.message}`);
    }
  };

  const handleDownload = async (id, fileName) => {
    try {
      const res = await axios.post(SERVER_URL + "/api/v1/profile/download-file", 
      { 
        fileId: id
      }, 
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
        responseType: "blob"
      });

      const url = window.URL.createObjectURL(new Blob([res.data]));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", fileName); 
      document.body.appendChild(link);
      link.click();
      link.remove();
    } catch (error) {
      //setResponse(`Error: ${error.response?.data?.message || error.message}`);
    }
  };

  const handleInfo = async (id) => {
    navigate("/file-info", { state: { fileId: id } })
  };

  const handleDelete = async (id) => {
    try {
      const res = await axios.post(SERVER_URL + "/api/v1/profile/delete", 
      { 
        fileId: id
      }, 
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
    } catch (error) {
      setResponse(`Error: ${error.response?.data?.message || error.message}`);
    }
  };

  let allocationStatus;
  if (profile && profile.reservatonActivated == true) {
    allocationStatus = "Yes";
  } else if (profile && profile.reservatonActivated == false) {
    allocationStatus = "Pending";
  } else {
    allocationStatus = "No";
  }

  return (
    <div>
      <h1>Profile</h1>
      {error && <p style={{ color: "red" }}>{error}</p>}

      {profile ? (
        <div>
          <p><strong>First Name:</strong> {profile.firstName}</p>
          <p><strong>Last Name:</strong> {profile.lastName}</p>
          <button onClick={handleLogout} style={{ marginTop: "20px" }}>
            Logout
          </button>
          {profile.authorities && profile.authorities.includes("ADMIN") ? (
            <button onClick={() => navigate("/admin")} style={{ marginTop: "20px" }}>
              Admin
            </button>
          ) : (
            <></>
          )
          }

          <h1>----------------------------------------</h1>
          <p><strong>Is space allocated? :</strong> {allocationStatus}</p>
          {profile.reservatonActivated ? (
            <>
              <p><strong>Allocated space :</strong> {profile.totalSize}mb</p>
              <p><strong>Occupied space :</strong> {profile.usedSize}mb</p>

              <h1>----------------------------------------</h1>

              <div style={{ marginTop: "20px" }}>
                <input type="file" onChange={handleFileChange} />
                <button onClick={handleUpload} style={{ marginLeft: "10px" }}>
                  Upload File
                </button>
              </div>

              <h1>----------------------------------------</h1>

              {profile && profile.files && profile.files.length > 0 && (
                <div>
                  <ul>
                    {profile.files.map((file, index) => (
                      <li key={index}>{file.fileName}
                        <button onClick={() => handleDownload(file.id, file.fileName)} style={{ marginLeft: "10px" }}>
                          Download
                        </button>
                        <button onClick={() => handleInfo(file.id)} style={{ marginLeft: "10px" }}>
                          Info
                        </button>
                        <button onClick={() => handleDelete(file.id)} style={{ marginLeft: "10px" }}>
                          Delete
                        </button>
                      </li>
                    ))}
                  </ul>
                </div>
              )}

            </>
          ) : (
            <>
              <p>
                <button onClick={() => handleAllocate(100)} style={{ marginLeft: "10px" }}>
                  100 mb
                </button>
                <button onClick={() => handleAllocate(200)} style={{ marginLeft: "10px" }}>
                  200 mb
                </button>
                <button onClick={() => handleAllocate(300)} style={{ marginLeft: "10px" }}>
                  300 mb
                </button>
              </p>
              <p style={{ color: "red" }}>{allocationResp}</p>
            </>
          )}
          <h1>----------------------------------------</h1>
        </div>
      ) : (
        !error && <p>Loading profile...</p>
      )}
    </div>
  );
};

export default Profile;
