/*import SearchBar from "./SearchBar";
import "./ListaUsuarios.css"
import React, { useEffect, useState } from "react";


interface Usuario {
  id: number;
  nombre: string;
  apellido: string;
  email: string;
  rol: string;
}

const TablaUsuarios: React.FC = () => {
  const [usuarios, setUsuarios] = useState<Usuario[]>([]);
  const [editandoId, setEditandoId] = useState<number | null>(null);
  const [formData, setFormData] = useState<Partial<Usuario>>({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [search, setSearch] = useState("");  //esto lo uso para filtrar por nombre

  // Recuperar token de localStorage
  const token = localStorage.getItem("token");

  useEffect(() => {
    if (!token) return;

    fetch("http://localhost:8080/api/usuario/lista", {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then((res) => {
        if (!res.ok) throw new Error('Error al cargar usuarios');
        return res.json();
      })
      .then((data) => {
        setUsuarios(data);
        setLoading(false);
      })
      .catch((err) => {
        console.error("Error al cargar usuarios:", err);
        setError("Error al cargar la lista de usuarios");
        setLoading(false);
      });
  }, [token]);

  const handleEdit = (usuario: Usuario) => {
    setEditandoId(usuario.id);
    setFormData(usuario);
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSave = async (id: number) => {
    if (!token) return;
    try {
      const res = await fetch(`http://localhost:8080/api/usuario/${id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(formData),
      });

      if (res.ok) {
        setUsuarios((prev) =>
          prev.map((u) => (u.id === id ? { ...u, ...formData } as Usuario : u))
        );
        setEditandoId(null);
      } else {
        console.error("Error al actualizar usuario");
      }
    } catch (error) {
      console.error("Error al guardar usuario:", error);
    }
  };

  const handleCancel = () => {
    setEditandoId(null);
    setFormData({});
  };

  const handleDelete = async (id: number) => {
    if (!token) return;

    const confirmar = window.confirm("⚠️ ¿Estás seguro de que deseas eliminar este usuario?");
    if (!confirmar) return;

    try {
      const res = await fetch(`http://localhost:8080/api/usuario/${id}`, {
        method: "DELETE",
        headers: { Authorization: `Bearer ${token}` },
      });

      if (res.ok) {
        setUsuarios((prev) => prev.filter((u) => u.id !== id));
      } else {
        console.error("Error al eliminar usuario");
      }
    } catch (error) {
      console.error("Error al eliminar usuario:", error);
    }
  };

  const getRoleBadgeClass = (rol: string) => {
    switch (rol) {
      case 'ADMINISTRADOR': return 'role-badge admin';
      case 'BIBLIOTECARIO': return 'role-badge bibliotecario';
      case 'USUARIO_REGISTRADO': return 'role-badge usuario';
      default: return 'role-badge';
    }
  };

  const getRoleDisplayName = (rol: string) => {
    switch (rol) {
      case 'ADMINISTRADOR': return 'Admin';
      case 'BIBLIOTECARIO': return 'Bibliotecario';
      case 'USUARIO_REGISTRADO': return 'Usuario';
      default: return rol;
    }
  };

  if (loading) {
    return (
      <div className="users-container">
        <div className="loading-state">
          <div className="loading-spinner"></div>
          Cargando usuarios...
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="users-container">
        <div className="alert alert-error">
          {error}
        </div>
      </div>
    );
  }

   const usuariosFiltrados = usuarios.filter((u) =>
    `${u.nombre} ${u.apellido} ${u.email} ${u.rol}`
      .toLowerCase()
      .includes(search.toLowerCase())
  );

  return (
    <div className="users-container">
      <div className="users-header">
        <h2 className="users-title">Lista de Usuarios</h2>
        
        <SearchBar
          value={search}
          onChange={setSearch}
          placeholder="Buscar por nombre, email o rol..."
        />
       
      </div>
      
      <div className="users-table-container">
        <table className="users-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Nombre</th>
              <th>Apellido</th>
              <th>Email</th>
              <th>Rol</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {usuarios.map((u) =>
              editandoId === u.id ? (
                <tr key={u.id} className="editing">
                  <td>
                    <span className="user-id">#{u.id}</span>
                  </td>
                  <td>
                    <input 
                      className="table-input"
                      name="nombre" 
                      value={formData.nombre || ""} 
                      onChange={handleChange}
                      placeholder="Nombre"
                    />
                  </td>
                  <td>
                    <input 
                      className="table-input"
                      name="apellido" 
                      value={formData.apellido || ""} 
                      onChange={handleChange}
                      placeholder="Apellido"
                    />
                  </td>
                  <td>
                    <input 
                      className="table-input"
                      name="email" 
                      value={formData.email || ""} 
                      onChange={handleChange}
                      placeholder="Email"
                      type="email"
                    />
                  </td>
                  <td>
                    <select 
                      className="table-select"
                      name="rol" 
                      value={formData.rol || ""} 
                      onChange={handleChange}
                    >
                      <option value="ADMINISTRADOR">Administrador</option>
                      <option value="BIBLIOTECARIO">Bibliotecario</option>
                      <option value="USUARIO_REGISTRADO">Usuario</option>
                    </select>
                  </td>
                  <td>
                    <div className="action-buttons">
                      <button 
                        className="action-btn action-btn-save"
                        onClick={() => handleSave(u.id)}
                      >
                        ✓ Guardar
                      </button>
                      <button 
                        className="action-btn action-btn-cancel"
                        onClick={handleCancel}
                      >
                        ✕ Cancelar
                      </button>
                    </div>
                  </td>
                </tr>
              ) : (
                <tr key={u.id}>
                  <td>
                    <span className="user-id">#{u.id}</span>
                  </td>
                  <td>{u.nombre}</td>
                  <td>{u.apellido}</td>
                  <td>{u.email}</td>
                  <td>
                    <span className={getRoleBadgeClass(u.rol)}>
                      {getRoleDisplayName(u.rol)}
                    </span>
                  </td>
                  <td>
                    <div className="action-buttons">
                      <button 
                        className="action-btn action-btn-edit"
                        onClick={() => handleEdit(u)}
                      >
                        ✏️ Editar
                      </button>
                      <button 
                        className="action-btn action-btn-delete"
                        onClick={() => handleDelete(u.id)}
                      >
                        🗑️ Eliminar
                      </button>
                    </div>
                  </td>
                </tr>
              )
            )}
          </tbody>
        </table>
      </div>
      
      {usuarios.length === 0 && (
        <div className="loading-state">
          No hay usuarios registrados
        </div>
      )}
    </div>
  );
};

export default TablaUsuarios;*/


import { Users } from "lucide-react";
import "./ListaUsuarios.css";
import React, { useEffect, useState } from "react";
import SearchBar from "./SearchBar";

interface Usuario {
  id: number;
  nombre: string;
  apellido: string;
  email: string;
  rol: string;
}

const TablaUsuarios: React.FC = () => {
  const [usuarios, setUsuarios] = useState<Usuario[]>([]);
  const [editandoId, setEditandoId] = useState<number | null>(null);
  const [formData, setFormData] = useState<Partial<Usuario>>({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [searchTerm, setSearchTerm] = useState("");

  // Recuperar token de localStorage
  const token = localStorage.getItem("token");

  useEffect(() => {
    if (!token) return;

    fetch("http://localhost:8080/api/usuario/lista", {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then((res) => {
        if (!res.ok) throw new Error("Error al cargar usuarios");
        return res.json();
      })
      .then((data) => {
        setUsuarios(data);
        setLoading(false);
      })
      .catch((err) => {
        console.error("Error al cargar usuarios:", err);
        setError("Error al cargar la lista de usuarios");
        setLoading(false);
      });
  }, [token]);

  const handleEdit = (usuario: Usuario) => {
    setEditandoId(usuario.id);
    setFormData(usuario);
  };

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSave = async (id: number) => {
    if (!token) return;
    try {
      const res = await fetch(`http://localhost:8080/api/usuario/${id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(formData),
      });

      if (res.ok) {
        setUsuarios((prev) =>
          prev.map((u) =>
            u.id === id ? ({ ...u, ...formData } as Usuario) : u
          )
        );
        setEditandoId(null);
      } else {
        console.error("Error al actualizar usuario");
      }
    } catch (error) {
      console.error("Error al guardar usuario:", error);
    }
  };

  const handleCancel = () => {
    setEditandoId(null);
    setFormData({});
  };

  const handleDelete = async (id: number) => {
    if (!token) return;

    const confirmar = window.confirm(
      "⚠️ ¿Estás seguro de que deseas eliminar este usuario?"
    );
    if (!confirmar) return;

    try {
      const res = await fetch(`http://localhost:8080/api/usuario/${id}`, {
        method: "DELETE",
        headers: { Authorization: `Bearer ${token}` },
      });

      if (res.ok) {
        setUsuarios((prev) => prev.filter((u) => u.id !== id));
      } else {
        console.error("Error al eliminar usuario");
      }
    } catch (error) {
      console.error("Error al eliminar usuario:", error);
    }
  };

  const getRoleBadgeClass = (rol: string) => {
    switch (rol) {
      case "ADMINISTRADOR":
        return "role-badge admin";
      case "BIBLIOTECARIO":
        return "role-badge bibliotecario";
      case "USUARIO_REGISTRADO":
        return "role-badge usuario";
      default:
        return "role-badge";
    }
  };

  const getRoleDisplayName = (rol: string) => {
    switch (rol) {
      case "ADMINISTRADOR":
        return "Admin";
      case "BIBLIOTECARIO":
        return "Bibliotecario";
      case "USUARIO_REGISTRADO":
        return "Usuario";
      default:
        return rol;
    }
  };

  // 🔍 Filtrado con fix
  const search = searchTerm.trim().toLowerCase();
  const filteredUsuarios = usuarios.filter(
    (u) =>
      u.nombre.toLowerCase().includes(search) ||
      u.apellido.toLowerCase().includes(search) ||
      u.email.toLowerCase().includes(search) ||
      u.rol.toLowerCase().includes(search)
  );

  if (loading) {
    return (
      <div className="users-container">
        <div className="loading-state">
          <div className="loading-spinner"></div>
          Cargando usuarios...
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="users-container">
        <div className="alert alert-error">{error}</div>
      </div>
    );
  }

  return (
    <div className="users-container">
      <div className="users-header">
        <h2 className="users-title">
          <Users className="users-icon" /> Lista de Usuarios
        </h2>
      </div>

      {/* Componente SearchBar */}
      <SearchBar value={searchTerm} onChange={setSearchTerm} />

      <div className="users-table-container">
        <table className="users-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Nombre</th>
              <th>Apellido</th>
              <th>Email</th>
              <th>Rol</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {filteredUsuarios.map((u) =>
              editandoId === u.id ? (
                <tr key={u.id} className="editing">
                  <td>
                    <span className="user-id">#{u.id}</span>
                  </td>
                  <td>
                    <input
                      className="table-input"
                      name="nombre"
                      value={formData.nombre || ""}
                      onChange={handleChange}
                      placeholder="Nombre"
                    />
                  </td>
                  <td>
                    <input
                      className="table-input"
                      name="apellido"
                      value={formData.apellido || ""}
                      onChange={handleChange}
                      placeholder="Apellido"
                    />
                  </td>
                  <td>
                    <input
                      className="table-input"
                      name="email"
                      value={formData.email || ""}
                      onChange={handleChange}
                      placeholder="Email"
                      type="email"
                    />
                  </td>
                  <td>
                    <select
                      className="table-select"
                      name="rol"
                      value={formData.rol || ""}
                      onChange={handleChange}
                    >
                      <option value="ADMINISTRADOR">Administrador</option>
                      <option value="BIBLIOTECARIO">Bibliotecario</option>
                      <option value="USUARIO_REGISTRADO">Usuario</option>
                    </select>
                  </td>
                  <td>
                    <div className="action-buttons">
                      <button
                        className="action-btn action-btn-save"
                        onClick={() => handleSave(u.id)}
                      >
                        ✓ Guardar
                      </button>
                      <button
                        className="action-btn action-btn-cancel"
                        onClick={handleCancel}
                      >
                        ✕ Cancelar
                      </button>
                    </div>
                  </td>
                </tr>
              ) : (
                <tr key={u.id}>
                  <td>
                    <span className="user-id">#{u.id}</span>
                  </td>
                  <td>{u.nombre}</td>
                  <td>{u.apellido}</td>
                  <td>{u.email}</td>
                  <td>
                    <span className={getRoleBadgeClass(u.rol)}>
                      {getRoleDisplayName(u.rol)}
                    </span>
                  </td>
                  <td>
                    <div className="action-buttons">
                      <button
                        className="action-btn action-btn-edit"
                        onClick={() => handleEdit(u)}
                      >
                        ✏️ Editar
                      </button>
                      <button
                        className="action-btn action-btn-delete"
                        onClick={() => handleDelete(u.id)}
                      >
                        🗑️ Eliminar
                      </button>
                    </div>
                  </td>
                </tr>
              )
            )}
          </tbody>
        </table>
      </div>

      {filteredUsuarios.length === 0 && (
        <div className="loading-state">No hay usuarios encontrados</div>
      )}
    </div>
  );
};

export default TablaUsuarios;
