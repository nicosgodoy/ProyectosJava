import React from "react";
import { useParams } from "react-router-dom";
import { useState, useEffect } from "react";
import "./DocumentoPreview.css";

type DocumentoPreviewProps = {
  token: string;
};


type DocumentoDTO = {
  titulo: string | null;
  autor: string;
  editorial: string;
  idioma: string;
};

const DocumentoPreview: React.FC<DocumentoPreviewProps> = ({ token }) => {
  const { id } = useParams<{ id: string }>();
  const [documento, setDocumento] = useState<DocumentoDTO | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // Cargar los datos del documento al montar el componente
  useEffect(() => {
    const cargarDocumento = async () => {
      try {
        console.log("Cargando documento con ID:", id);
        console.log("Token:", token);

        const response = await fetch(
          `http://localhost:8080/api/documento/${id}`,
          {
            method: 'GET',
            headers: {
              'Authorization': `Bearer ${token}`,
              'Content-Type': 'application/json',
            },
          }
        );

        console.log("Response status:", response.status);
        console.log("Response headers:", response.headers);

        if (response.status === 403) {
          throw new Error("Acceso denegado - Revisa la configuración de seguridad del backend");
        }

        if (response.status === 401) {
          throw new Error("No autorizado - Token inválido o expirado");
        }

        if (!response.ok) {
          throw new Error(`Error HTTP: ${response.status} - ${response.statusText}`);
        }

        const data: DocumentoDTO = await response.json();
        console.log("Datos recibidos:", data);
        setDocumento(data);
        setError(null);
      } catch (error) {
        console.error("No se pudo cargar el documento:", error);
        setError(error instanceof Error ? error.message : "Error desconocido");
      } finally {
        setLoading(false);
      }
    };

    if (id && token) {
      cargarDocumento();
    } else {
      setLoading(false);
      if (!token) setError("Token no disponible");
      if (!id) setError("ID no disponible");
    }
  }, [id, token]);

  const handleDescargar = async () => {
    try {
      const response = await fetch(
        `http://localhost:8080/api/documento/descargar/${id}`,
        {
          headers: {
            'Authorization': `Bearer ${token}`,
          },
        }
      );

      if (!response.ok) {
        throw new Error(`Error al descargar documento: ${response.status}`);
      }

      const blob = await response.blob();
      const url = window.URL.createObjectURL(blob);

      const a = document.createElement("a");
      a.href = url;
      a.download = `documento-${id}.pdf`;
      document.body.appendChild(a);
      a.click();
      a.remove();

      window.URL.revokeObjectURL(url);
    } catch (error) {
      console.error("No se pudo descargar el documento:", error);
      setError(error instanceof Error ? error.message : "Error al descargar");
    }
  };

  if (!id) return <p>No se encontró el documento</p>;
  if (loading) return <p>Cargando documento...</p>;

  return (
    <div className="documento-preview-container">
      <div className="documento-preview-header">
        <button onClick={handleDescargar} className="download-button">
          Descargar
        </button>
        
        {error && (
          <div className="error-message" style={{ color: "red", margin: "10px 0", padding: "10px", border: "1px solid red" }}>
            <strong>Error:</strong> {error}
          </div>
        )}
        
        {documento ? (
          <div className="documento-metadata" style={{ marginTop: '20px' }}>
            <p><strong>Título:</strong> {documento.titulo || "Sin título"}</p>
            <p><strong>Autor:</strong> {documento.autor || "Sin autor"}</p>
            <p><strong>Editorial:</strong> {documento.editorial || "Sin editorial"}</p>
            <p><strong>Idioma:</strong> {documento.idioma || "Sin idioma"}</p>
          </div>
        ) : !error && (
          <p>No se pudieron cargar los datos del documento</p>
        )}
      </div>
      
      {/* Mostrar el iframe solo si no hay error de autenticación */}
      {!error && (
        
        <iframe
          src={`http://localhost:8080/api/documento/previsualizar/${id}`}
          title={`Documento ${id}`}
          className="documento-preview-iframe"
          onError={(e) => {
            console.error("Error cargando el iframe:", e);
            setError("Error al cargar la vista previa del documento");
          }}
        />
      )}
    </div>
  );
};

export default DocumentoPreview;