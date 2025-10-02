import React, { useState, useEffect } from 'react';
import './ListaRoles.css';

interface RolDTO {
  id: number;
  rol: string;
}

interface ListaRolesProps {
  token: string;
}

const ListaRoles: React.FC<ListaRolesProps> = ({ token }) => {
  const [roles, setRoles] = useState<RolDTO[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const getRolConfig = (rolName: string) => {
    switch (rolName) {
      case 'ADMINISTRADOR':
        return {
          descripcion: 'Control total del sistema, gestión de usuarios y configuraciones'
        };
      case 'BIBLIOTECARIO':
        return {
          descripcion: 'Gestión de documentos PDF, indexación y organización de contenido'
        };
      case 'USUARIO_REGISTRADO':
        return {
          descripcion: 'Carga de documentos, visualización y descarga de contenido'
        };
      default:
        return {
          descripcion: 'Rol personalizado del sistema'
        };
    }
  };

  useEffect(() => {
    const endpointUrl = 'http://localhost:8080/api/rol/get-all';
    
    fetch(endpointUrl, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json',
      },
    })
      .then(response => {
        if (!response.ok) {
          throw new Error('Error al cargar los roles');
        }
        return response.json();
      })
      .then((data: RolDTO[]) => {
        setRoles(data);
        setLoading(false);
      })
      .catch(error => {
        setError(error.message);
        setLoading(false);
      });
  }, [token]);

  if (loading) {
    return (
      <div className="loading">
        <div className="loading-content">
          Cargando roles del sistema...
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="error">
        <strong>Error al cargar los roles:</strong> {error}
      </div>
    );
  }

  return (
    <div className="lista-roles-container">
      <h1>
        Gestión de Roles del Sistema
      </h1>
      <p className="lista-roles-subtitle">
        Visualización de los roles de acceso configurados en DocIntelia
      </p>

      {roles.length > 0 ? (
        <div className="roles-grid">
          {roles.map((rol) => {
            const config = getRolConfig(rol.rol);
            return (
              <div key={rol.id} className="role-card">
                <div className="role-header">
                  <div>
                    <h3 className="role-title">
                      {rol.rol.replace(/_/g, ' ')}
                    </h3>
                    <div className="role-id">
                      ID: {rol.id}
                    </div>
                  </div>
                </div>
                
                <p className="role-description">
                  {config.descripcion}
                </p>
                
                <div className="role-status">
                  Rol Activo en el Sistema
                </div>
              </div>
            );
          })}
        </div>
      ) : (
        <div className="empty-state">
          <h3 className="empty-state-title">
            No hay roles configurados
          </h3>
          <p className="empty-state-text">
            El sistema no tiene roles configurados actualmente
          </p>
        </div>
      )}
    </div>
  );
};

export default ListaRoles;