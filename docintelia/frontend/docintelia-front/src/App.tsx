import React, { useState } from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";

import Login from "./components/Login.tsx";
import ListaUsuarios from "./components/ListaUsuarios.tsx";
import ListaDocumentos from "./components/ListaDocumentos.tsx";
import DocumentoPreview from "./components/DocumentoPreview.tsx"; 
import GestionDocumentos from "./components/GestionDocumentos.tsx";
import SubirDocumento from "./components/SubirDocumento.tsx";
import RegistrarUsuario from "./components/RegistrarUsuario.tsx";
import ListaRoles from "./components/ListaRoles.tsx";

import "./App.css"; // ✅ Importa estilos globales
import PrivateRoute from "./components/PrivateRoutes.tsx";
import Navbar from "./components/Navbar.tsx";

const App: React.FC = () => {
  const [token, setToken] = useState<string | null>(() => localStorage.getItem("token"));
  const [rol, setRol] = useState<string | null>(() => localStorage.getItem("rol"));

  return (
    <Router>
      <div className="app-container">
        {/* Header fijo - solo se muestra cuando hay token */}
        {token && (
          <header>
            <h1>Docintelia</h1>      
            <Navbar/>
          </header>
        )}

        {/* Contenido con scroll independiente */}
        <main className={`main-content ${!token ? 'login-mode' : ''}`}>
          {!token ? (
            <Login 
  onLoginSuccess={(token, rol) => {
    setToken(token);
    setRol(rol);
  }} 
/>
          ) : (
            <Routes>
  <Route path="/" element={<Navigate to="/documentos" />} />



  <Route path="/registar-usuario" element={
    <PrivateRoute token={token} rol={rol} allowedRoles={["ADMINISTRADOR"]}>
      <div className="scrollable-route">
        <RegistrarUsuario token={token} />
      </div>
    </PrivateRoute>
  }/>

  <Route path="/listar-usuarios" element={
    <PrivateRoute token={token} rol={rol} allowedRoles={["ADMINISTRADOR"]}>
      <div className="scrollable-route">
        <ListaUsuarios />
      </div>
    </PrivateRoute>
  }/>

   <Route path="/usuarios" element={
    <PrivateRoute token={token} rol={rol} allowedRoles={["ADMINISTRADOR"]}>
      <div className="scrollable-route">
        <RegistrarUsuario token={token} />
         <ListaUsuarios />
      </div>
    </PrivateRoute>
  }/>

  <Route path="/documentos" element={
    <PrivateRoute token={token} rol={rol}>
      <div className="scrollable-route">
        <ListaDocumentos token={token} />
      </div>
    </PrivateRoute>
  }/>

  <Route
  path="/documento/:id"
  element={
    <PrivateRoute token={token} rol={rol}>
      <div className="route-container">
        <DocumentoPreview token={token!} /*id={0}*/ />
      </div>
    </PrivateRoute>
  }
/>

  <Route path="/gestion-documentos" element={
    <PrivateRoute token={token} rol={rol} allowedRoles={["ADMINISTRADOR","BIBLIOTECARIO"]}>
      <div className="scrollable-route">
        <GestionDocumentos token={token} />
      </div>
    </PrivateRoute>
  }/>

  <Route path="/subir-documento" element={
    <PrivateRoute token={token} rol={rol} allowedRoles={["ADMINISTRADOR","BIBLIOTECARIO"]}>
      <div className="scrollable-route">
        <SubirDocumento token={token!} />
      </div>
    </PrivateRoute>
  }/>

  <Route path="/listar-roles" element={
    <PrivateRoute token={token} rol={rol} allowedRoles={["ADMINISTRADOR"]}>
      <div className="scrollable-route">
        <ListaRoles token={token!} />
      </div>
    </PrivateRoute>
  }/>
</Routes>

          )}
        </main>
      </div>
    </Router>
  );
};

export default App;