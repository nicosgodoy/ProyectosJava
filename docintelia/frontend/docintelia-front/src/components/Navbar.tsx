import React, { useState } from 'react'
import DropdownButton from './ui/DropdownButton';
import type { MenuItem } from '../types';
import "./Navbar.css";
import {Link, useNavigate} from 'react-router-dom';
import { MdOutlineListAlt } from "react-icons/md";
import { IoMdPersonAdd } from "react-icons/io";
import { GrDocumentUpdate } from "react-icons/gr";
import AvatarDropdown from './AvatarDropdown';

const Navbar: React.FC = () => {

const navigate = useNavigate();
const [token, setToken] = useState<string | null>(() => localStorage.getItem("token"));
const [rol, setRol] = useState<string | null>(() => localStorage.getItem("rol"));

  const handleLogout = () => {
 localStorage.removeItem("token");
localStorage.removeItem("rol");
 setToken(null);
 setRol(null);

  window.location.href = "/"; 
  }

  // Ejemplo de items del menú
  const menuPerfil: MenuItem[] = [
    {
      id: 'profile',
      label: 'Mi Perfil',
      onClick: () => console.log('Ir a perfil'),
      icon: '👤'
    },
    {
      id: 'datos',
      label: 'Modificar datos',
      onClick: () => console.log("modificar datos"),
      icon: '⚙️'
    },
    {
      id: 'logout',
      label: 'Cerrar Sesión',
      onClick: () => handleLogout(),
      icon: '🚪'
    }
  ];

  const menuUsuario: MenuItem[] = [
    {
      id: 'new-project',
      label: 'registrar Usuario',
      onClick: () => navigate("/registar-usuario"),
      icon:  <IoMdPersonAdd />                
    },
    {
      id: 'open-project',
      label: 'Listar Usuarios',
      onClick: () => navigate("/listar-usuarios"),
      icon: <MdOutlineListAlt />                         
    },
    {
      id: 'save-project',
      label: 'Listar roles',
      onClick: () => navigate("/listar-roles"),
      icon: '🚪'
    }
  ];

   const menuBiblioteca: MenuItem[] = [
    {
      id: 'new-project',
      label: 'Gestion de documentos',
      onClick: () => navigate("/gestion-documentos"),
      icon:  '🚪'
    },
    {
      id: 'open-project',
      label: 'Subir documento',
      onClick: () => navigate("/subir-documento"), 
      icon: <GrDocumentUpdate />
    },
  ];


  return (
    <nav className="navbar">    
      
      {rol==="ADMINISTRADOR" && 
        <DropdownButton
          buttonText="Usuarios"
          menuItems={menuUsuario}
          position="left"
          variant="outline"
        />}
        {(rol==="ADMINISTRADOR" || rol==="BIBLIOTECARIO") &&
        <DropdownButton
          buttonText="Biblioteca"
          menuItems={menuBiblioteca}
          position="left"
          variant="outline"
        />}

         <Link to="/documentos" className="  nav-button outline" >Buscar documentos</Link>

       
        <AvatarDropdown
            position="right"
            menuItems={menuPerfil}
        />      
    </nav>
  );
}

export default Navbar;