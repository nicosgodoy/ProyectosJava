import { Navigate } from "react-router-dom";

type PrivateRouteProps = {
  children: React.ReactNode;
  token: string | null;
  rol: string | null;
  allowedRoles?: string[];
};

const PrivateRoute = ({ children, token, rol, allowedRoles }: PrivateRouteProps) => {
  if (!token) {
    return <Navigate to="/" replace />;
  }

  if (allowedRoles && !allowedRoles.includes(rol!)) {
    return <Navigate to="/documentos" replace />; // 👈 Redirigimos si el rol no tiene permiso
  }

  return <>{children}</>;
};

export default PrivateRoute;
