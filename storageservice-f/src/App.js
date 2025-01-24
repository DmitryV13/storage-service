import React from "react";
import SignUp from "./api/SignUp";
import SignIn from "./api/SignIn";
import Profile from "./api/Profile";
import AdminPage from "./api/AdminPage";
import AccountVerification from "./api/AccountVerrification";
import FileInfo from "./api/FileInfo";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import { AuthProvider, useAuth } from "./api/AuthContext";

function App() {
  return (
    <AuthProvider>
      <Router>
        <h1>----------------------------------------</h1>
        <h1>Navigation</h1>
        <ul>
          <li><Link to="/signup">Sign Up</Link></li>
          <li><Link to="/signin">Sign In</Link></li>
          <li><Link to="/profile">Profile</Link></li>
        </ul>
        <h1>----------------------------------------</h1>

        <Routes>
          <Route path="/signup" element={<SignUp />} />
          <Route path="/signin" element={<SignIn />} />
          <Route path="/profile" element={<Profile />} />
          <Route path="/admin" element={<AdminPage />} />
          <Route path="/account-verification" element={<AccountVerification />} />
          <Route path="/file-info" element={<FileInfo />} />
        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App;
