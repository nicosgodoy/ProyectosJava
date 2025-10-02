// Login.tsx
import React, { useState } from "react";
import type { AuthRequest, AuthResponse } from "../types";
import './Login.css'

type LoginProps = {
  onLoginSuccess: (token: string, rol: string) => void;
};


const Login = ({ onLoginSuccess }: LoginProps) => {
  const [email, setEmail] = useState("admin@admin.com");
  const [password, setPassword] = useState("admin123");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError("");
    
    const authData: AuthRequest = { email, password };

    try {
      const response = await fetch("http://localhost:8080/api/auth/authenticate", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(authData),
      });

      if (!response.ok) throw new Error("Credenciales incorrectas");

      const data: AuthResponse = await response.json();

      // Guardamos el token en localStorage ( persiste al cerrar el navegador)
      localStorage.setItem("token", data.token);
      localStorage.setItem("rol", data.rol); 
      onLoginSuccess(data.token, data.rol); 

    } catch {
      setError("Error al iniciar sesión. Verifique sus credenciales.");
    } finally {
      setLoading(false);
    }
  };

  return (
    
    <div className="login-container">
      <div className="login-welcome">
      <h2>¡Bienvenido!</h2>
      <p>
        <strong>Docintelia</strong> es la plataforma que facilita la gestión,
        búsqueda y previsualización de documentos digitales. <strong>Inicie sesión</strong> para acceder
        a su biblioteca y aprovechar todas las funciones disponibles.
      </p>
    </div>
      <div className="login-card">
        <div className="login-header">
          <h1 className="login-title">Docintelia</h1>
          <p className="login-subtitle">Ingrese sus credenciales para acceder</p>
        </div>
        
        <form className="login-form" onSubmit={handleLogin}>
          <div className="form-group">
            <label className="form-label" htmlFor="email">Email</label>
            <input
              id="email"
              type="email"
              className="form-input"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="admin@admin.com"
              required
            />
          </div>
          
          <div className="form-group">
            <label className="form-label" htmlFor="password">Contraseña</label>
            <input
              id="password"
              type="password"
              className="form-input"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="••••••••"
              required
            />
          </div>
          
          <button 
            type="submit" 
            className="login-button"
            disabled={loading}
          >
            {loading ? "Ingresando..." : "Ingresar"}
          </button>
        </form>
        
        {error && (
          <div className="login-error">
            {error}
          </div>
        )}
      </div>
    </div>
  );
};

export default Login;