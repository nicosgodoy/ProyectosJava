import React, { useState, useRef, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import type { MenuItem } from '../types';

interface AvatarDropdownProps {
  menuItems?: MenuItem[];
  position?: "left" | "right";
}

const AvatarDropdown: React.FC<AvatarDropdownProps> = ({ 
  menuItems = [], 
  position = "right" 
}) => {
    const [isOpen, setIsOpen] = useState<boolean>(false);
    const dropdownRef = useRef<HTMLDivElement>(null);
    const navigate = useNavigate();

    const toggleDropdown = (): void => {
        console.log('Items disponibles:', menuItems);
        setIsOpen(!isOpen);
    };

    const handleItemClick = (item: MenuItem): void => {
        if (item.onClick) {
            item.onClick();
        }
        setIsOpen(false);
    };

    // Items FIJOS para asegurar que se vean
    const defaultMenuItems: MenuItem[] = [
        { 
            id: 'profile',
            label: 'Mi Perfil', 
            onClick: () => navigate('/perfil')
        },
        { 
            id: 'logout',
            label: 'Cerrar Sesión', 
            onClick: () => {
                localStorage.removeItem("token");
                localStorage.removeItem("rol");
                window.location.href = "/";
            }
        }
    ];

    // Usar items fijos para testing
    const itemsToUse = defaultMenuItems;

    useEffect(() => {
        const handleClickOutside = (event: MouseEvent): void => {
            if (dropdownRef.current && !dropdownRef.current.contains(event.target as Node)) {
                setIsOpen(false);
            }
        };

        document.addEventListener('mousedown', handleClickOutside);
        return () => document.removeEventListener('mousedown', handleClickOutside);
    }, []);

    return (
        <div className={`avatar-dropdown ${position}`} ref={dropdownRef}>
            {/* Avatar */}
            <div className="avatar-container" onClick={toggleDropdown}>
    <div className="avatar">
        <img 
            src="https://cdn-icons-png.flaticon.com/512/149/149071.png" 
            alt="Usuario" 
            style={{
                width: '36px',
                height: '36px',
                borderRadius: '50%'
            }}
        />
    </div>
</div>

            {/* Dropdown - VERSIÓN GARANTIZADA */}
            {isOpen && (
                <div className="dropdown-menu" style={{
                    position: 'absolute',
                    top: '100%',
                    right: '0',
                    background: 'white',
                    border: '2px solid #667eea',
                    borderRadius: '8px',
                    padding: '10px 0',
                    minWidth: '200px',
                    zIndex: 10000,
                    marginTop: '5px',
                    boxShadow: '0 4px 12px rgba(0,0,0,0.1)'
                }}>
                    {/* Item 1 - Fijo */}
                    <div 
                        className="dropdown-item"
                        onClick={() => navigate('/perfil')}
                        style={{
                            padding: '12px 16px',
                            cursor: 'pointer',
                            display: 'flex',
                            alignItems: 'center',
                            gap: '10px'
                            
                        }}
                    >
                        <span>👤</span>
                        <span style={{color:"blue"}}>Mi Perfil</span>
                    </div>
                    
                    {/* Item 2 - Fijo */}
                    <div 
                        className="dropdown-item"
                        onClick={() => {
                            localStorage.removeItem("token");
                            localStorage.removeItem("rol");
                            window.location.href = "/";
                        }}
                        style={{
                            padding: '12px 16px',
                            cursor: 'pointer',
                            display: 'flex',
                            alignItems: 'center',
                            gap: '10px',
                            color: '#e74c3c',
                            borderTop: '1px solid #f0f0f0'
                        }}
                    >
                        <span>🚪</span>
                        <span>Cerrar Sesión</span>
                    </div>

                    {/* Items dinámicos por si acaso */}
                    {itemsToUse.map((item) => (
                        <div
                            key={item.id}
                            className="dropdown-item"
                            onClick={() => handleItemClick(item)}
                            style={{
                                padding: '12px 16px',
                                cursor: 'pointer',
                                display: 'flex',
                                alignItems: 'center',
                                gap: '10px',
                              
                            }}
                        >
                            {item.icon && <span>{item.icon}</span>}
                            <span>{item.label}</span>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default AvatarDropdown;