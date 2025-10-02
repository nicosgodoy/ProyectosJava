import React, { useState } from "react";
import "./RegistrarUsuario.css";

interface Props {
  token: string; // token pasado desde App
}

interface RegisterForm {
  nombre: string;
  apellido: string;
  email: string;
  contrasenia: string;
  rol: string;
  id_biblioteca: number;
}

const RegistrarUsuario: React.FC<Props> = ({ token }) => {
  const [formData, setFormData] = useState<RegisterForm>({
    nombre: "",
    apellido: "",
    email: "",
    contrasenia: "",
    rol: "USUARIO_REGISTRADO", // valor por defecto
    id_biblioteca: 1, // fijo en 1
  });

  const [mensaje, setMensaje] = useState("");
  const [tipoMensaje, setTipoMensaje] = useState<"success" | "error" | "info">("info");
  const [loading, setLoading] = useState(false);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: name === 'id_biblioteca' ? parseInt(value) : value,
    });
    
    // Limpiar mensaje cuando el usuario empiece a escribir
    if (mensaje) {
      setMensaje("");
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setMensaje("");

    if (!token) {
      setMensaje("Token inválido o expirado");
      setTipoMensaje("error");
      setLoading(false);
      return;
    }

    try {
      const response = await fetch("http://localhost:8080/api/auth/register", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}`,
        },
        body: JSON.stringify(formData),
      });

      if (!response.ok) {
        const text = await response.text();
        console.error("Error del backend:", text);
        setMensaje("Error del servidor: " + text);
        setTipoMensaje("error");
        setLoading(false);
        return;
      }

      setMensaje("Usuario registrado correctamente");
      setTipoMensaje("success");
      
      // Limpiar formulario después del éxito
      setFormData({
        nombre: "",
        apellido: "",
        email: "",
        contrasenia: "",
        rol: "USUARIO_REGISTRADO",
        id_biblioteca: 1,
      });
      
      // Limpiar mensaje después de 3 segundos
      setTimeout(() => {
        setMensaje("");
      }, 3000);
      
    } catch (error) {
      console.error("Error de conexión:", error);
      setMensaje("Error de conexión con el servidor");
      setTipoMensaje("error");
    } finally {
      setLoading(false);
    }
  };

  const getRoleDisplayName = (rol: string) => {
    switch (rol) {
      case 'ADMINISTRADOR': return 'Administrador';
      case 'BIBLIOTECARIO': return 'Bibliotecario';
      case 'USUARIO_REGISTRADO': return 'Usuario Registrado';
      default: return rol;
    }
  };

  const getRolePreviewClass = (rol: string) => {
    switch (rol) {
      case 'ADMINISTRADOR': return 'role-preview admin';
      case 'BIBLIOTECARIO': return 'role-preview bibliotecario';
      case 'USUARIO_REGISTRADO': return 'role-preview usuario';
      default: return 'role-preview';
    }
  };

  return (
    <div className="register-container">
      <div className="register-header">
        <h2 className="register-title">Registro de Usuario</h2>
        <p className="register-subtitle">
          Complete los datos para crear una nueva cuenta de usuario
        </p>
      </div>

      <div className="register-form">
        {mensaje && (
          <div className={`alert alert-${tipoMensaje}`}>
            {mensaje}
          </div>
        )}

        <form onSubmit={handleSubmit}>
          <div className="form-grid">
            <div className="form-group">
              <label className="form-label required" htmlFor="nombre">
                Nombre
              </label>
              <input
                id="nombre"
                type="text"
                name="nombre"
                className="form-input"
                placeholder="Ingrese el nombre"
                value={formData.nombre}
                onChange={handleChange}
                required
                disabled={loading}
              />
            </div>

            <div className="form-group">
              <label className="form-label required" htmlFor="apellido">
                Apellido
              </label>
              <input
                id="apellido"
                type="text"
                name="apellido"
                className="form-input"
                placeholder="Ingrese el apellido"
                value={formData.apellido}
                onChange={handleChange}
                required
                disabled={loading}
              />
            </div>

            <div className="form-group form-group-full">
              <label className="form-label required" htmlFor="email">
                Correo Electrónico
              </label>
              <input
                id="email"
                type="email"
                name="email"
                className="form-input"
                placeholder="usuario@ejemplo.com"
                value={formData.email}
                onChange={handleChange}
                required
                disabled={loading}
              />
            </div>

            <div className="form-group">
              <label className="form-label required" htmlFor="contrasenia">
                Contraseña
              </label>
              <input
                id="contrasenia"
                type="password"
                name="contrasenia"
                className="form-input"
                placeholder="••••••••"
                value={formData.contrasenia}
                onChange={handleChange}
                required
                disabled={loading}
                minLength={6}
              />
            </div>

            <div className="form-group">
              <label className="form-label required" htmlFor="rol">
                Rol
              </label>
              <select
                id="rol"
                name="rol"
                value={formData.rol}
                onChange={handleChange}
                className="form-select"
                disabled={loading}
              >
                <option value="ADMINISTRADOR">Administrador</option>
                <option value="BIBLIOTECARIO">Bibliotecario</option>
                <option value="USUARIO_REGISTRADO">Usuario Registrado</option>
              </select>
              <div className={getRolePreviewClass(formData.rol)}>
                {getRoleDisplayName(formData.rol)}
              </div>
            </div>
          </div>

          <button
            type="submit"
            className="register-button"
            disabled={loading}
          >
            {loading ? "Registrando..." : "Registrar Usuario"}
          </button>
        </form>
      </div>
    </div>
  );
};

export default RegistrarUsuario;