export interface Usuario {
  id: number;
  nombre: string;
  apellido: string;
  email: string;
  rol: string;
}

export interface AuthResponse {
  token: string;
  rol:"ADMINISTRADOR" | "BIBLIOTECARIO" | "USUARIO_REGISTRADO";
}

export interface AuthRequest {
  email: string;
  password: string;
}

/*Tipos para el navbar*/

export interface MenuItem {
  id: string;
  label: string;
  onClick: () => void;
  icon?: string | React.ReactNode;
   danger?: boolean;  //esto se agrego para el avatar
}

export interface DropdownButtonProps {
  buttonText: string;
  menuItems: MenuItem[];
  position?: 'left' | 'right';
  variant?: 'primary' | 'secondary' | 'outline';
}