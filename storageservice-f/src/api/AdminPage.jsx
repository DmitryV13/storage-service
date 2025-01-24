import React, { useEffect, useState } from "react";
import axios from "axios";
import { useAuth } from "./AuthContext";

const SERVER_URL = "http://localhost:8081";

const AdminPage = () => {
  const { token } = useAuth();
  const [users, setUsers] = useState([]);
  const [error, setError] = useState("");

  const fetchUsersInfo = async () => {
    try {
      const profileRes = await axios.get(SERVER_URL + "/api/v1/admin", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
        withCredentials: true,
      });
      setUsers(profileRes.data.users);
    } catch (err) {
      console.error("Error fetching users:", err);
      setError("Failed to fetch users. Please try again later.");
    }
  };

  const handleUnblockReservation = async (login) => {
    try {
      const res = await axios.get(SERVER_URL + "/api/v1/admin/unblock-reservation",  {
        headers: {
          Authorization: `Bearer ${token}`,
        },
        params: { username: login },
      });
    } catch (error) {
      //setResponse(`Error: ${error.response?.data?.message || error.message}`);
    }
  };

  const handleBlockReservation = async (login) => {
    try {
      const res = await axios.get(SERVER_URL + "/api/v1/admin/block-reservation",  {
        headers: {
          Authorization: `Bearer ${token}`,
        },
        params: { username: login },
      });
    } catch (error) {
      //setResponse(`Error: ${error.response?.data?.message || error.message}`);
    }
  };

  useEffect(() => {
    fetchUsersInfo();
  }, []);

  return (
    <div>
      <h1>Admin Page</h1>
      {error && <p style={{ color: "red" }}>{error}</p>}
      <table border="1" style={{ width: "100%", borderCollapse: "collapse" }}>
        <thead>
          <tr>
            <th>Login</th>
            <th>Role</th>
            <th>Account Activated</th>
            <th>Activation Key</th>
            <th>Reservation</th>
          </tr>
        </thead>
        <tbody>
          {users.length > 0 ? (
            users.map((user, index) => (
              <tr key={index}>
                <td>{user.login}</td>
                <td>{user.role.join(', ')}</td>
                <td>{user.accountActivated ? "Yes" : "No"}</td>
                <td>{user.activationKey || "N/A"}</td>
                <td>
                  <>
                    {(() => {
                      if (user.reservationActivated == null) {
                        return "No";
                      } else if (user.reservationActivated == false && user.reservationUsedSize==0) {
                        return <>{user.reservationTotalSize} mb requested 
                          <button onClick={() => handleUnblockReservation(user.login)} style={{ marginTop: "20px" }}>
                            Accept request
                          </button>
                        </>;
                      }if (user.reservationActivated == false && user.reservationUsedSize!=0) {
                        return <>{user.reservationTotalSize} mb requested 
                          <button onClick={() => handleUnblockReservation(user.login)} style={{ marginTop: "20px" }}>
                            Unblock reservation
                          </button>
                        </>;
                      } else {
                        return <>Yes  
                        <button onClick={() => handleBlockReservation(user.login)} style={{ marginTop: "20px" }}>
                          Block reservation
                        </button>
                      </>;
                      }
                    })()}
                  </>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="4" style={{ textAlign: "center" }}>
                No users found
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
};

export default AdminPage;
