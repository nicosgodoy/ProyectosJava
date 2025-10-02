import React, { useState, useRef, useEffect } from 'react';
import type { MenuItem, DropdownButtonProps } from '../../types';
import './DropdownButton.css';

const DropdownButton: React.FC<DropdownButtonProps> = ({
  buttonText,
  menuItems,
  position = 'left',
  variant = 'primary'
}) => {
  const [isOpen, setIsOpen] = useState(false);
  const dropdownRef = useRef<HTMLDivElement>(null);

  // Cerrar dropdown al hacer clic fuera
  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target as Node)) {
        setIsOpen(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  const toggleDropdown = () => setIsOpen(!isOpen);

  const handleMenuItemClick = (item: MenuItem) => {
    item.onClick();
    setIsOpen(false);
  };

  return (
    <div className={`dropdown-container ${position}`} ref={dropdownRef}>
      <button 
        className={`dropdown-button ${variant} ${isOpen ? 'active' : ''}`}
        onClick={toggleDropdown}
        aria-haspopup="true"
        aria-expanded={isOpen}
      >
        <span>{buttonText}</span>
        <svg 
          className={`dropdown-arrow ${isOpen ? 'rotate' : ''}`} 
          width="12" 
          height="12" 
          viewBox="0 0 12 12"
        >
          <path d="M3 4.5L6 7.5L9 4.5" stroke="currentColor" fill="none"/>
        </svg>
      </button>

      {isOpen && (
        <div className={`dropdown-menu ${position}`}>
          {menuItems.map((item) => (
            <button
              key={item.id}
              className="dropdown-menu-item"
              onClick={() => handleMenuItemClick(item)}
              role="menuitem"
            >
              {item.icon && <span className="menu-icon">{item.icon}</span>}
              <span className="menu-label">{item.label}</span>
            </button>
          ))}
        </div>
      )}
    </div>
  );
};

export default DropdownButton;