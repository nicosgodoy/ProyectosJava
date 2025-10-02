import React, { useState } from "react";
import type { Usuario } from "../types";

const BuscarUsuario: React.FC = () => {
  const [id, setId] = useState<string>("");
  const [usuario, setUsuario] = useState<Usuario | null>(null);
  const [error, setError] = useState<string>("");

  const handleBuscar = async () => {
    if (!id) {
      setError("Por favor ingrese un ID");
      return;
    }
    try {
      setError("");

      // Recuperamos el token desde sessionStorage
      const token = localStorage.getItem("token");

      if (!token) {
        setError("No hay token, inicie sesión primero");
        return;
      }

      const response = await fetch(`http://localhost:8080/api/usuario/find-by-id/${id}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}`, // 🔑 Aquí mandamos el JWT
        },
      });

      if (!response.ok) {
        throw new Error("Usuario no encontrado o no autorizado");
      }

      const data: Usuario = await response.json();
      setUsuario(data);
    }  catch (err: unknown) {
  if (err instanceof Error) {
    setError(err.message);
  } else {
    setError("Ocurrió un error desconocido");
  }
  setUsuario(null);
}

  };

  return (
    <div style={{ marginTop: "20px" }}>
      <h2>Buscar Usuario por ID</h2>
      <input
        type="text"
        value={id}
        onChange={(e) => setId(e.target.value)}
        placeholder="Ingrese ID"
      />
      <button onClick={handleBuscar} style={{marginLeft: "20px"}}>Buscar</button>

      {error && <p style={{ color: "red" }}>{error}</p>}

      {usuario && (
        <div style={{ marginTop: "10px", border: "1px solid #ccc", padding: "10px", color:"black" }}>
          <p><strong>ID:</strong> {usuario.id}</p>
          <p><strong>Nombre:</strong> {usuario.nombre}</p>
          <p><strong>Apellido:</strong> {usuario.apellido}</p>
        </div>
      )}
    </div>
  );
};

export default BuscarUsuario;
