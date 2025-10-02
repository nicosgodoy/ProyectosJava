import React, { useState, useRef } from "react";
import type { DragEvent } from "react";
import "./SubirDocumento.css";


type SubirDocumentoProps = {
  token: string; // JWT
};

const SubirDocumento: React.FC<SubirDocumentoProps> = ({ token }) => {
  const [file, setFile] = useState<File | null>(null);
  const [titulo, setTitulo] = useState("");
  const [autor, setAutor] = useState("");
  const [editorial, setEditorial] = useState("");
  const [idioma, setIdioma] = useState("");
  const [mensaje, setMensaje] = useState("");
  const [tipoMensaje, setTipoMensaje] = useState<"success" | "error">("success");
  const [loading, setLoading] = useState(false);
  const [dragOver, setDragOver] = useState(false);
  
  const fileInputRef = useRef<HTMLInputElement>(null);

  const formatFileSize = (bytes: number): string => {
    if (bytes === 0) return '0 Bytes';
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
  };

  const handleFileSelect = (selectedFile: File | null) => {
    if (!selectedFile) {
      setFile(null);
      return;
    }

    // Validar que sea PDF
    if (selectedFile.type !== 'application/pdf') {
      setMensaje("Solo se permiten archivos PDF");
      setTipoMensaje("error");
      return;
    }

    // Validar tamaño (máximo 10MB)
    if (selectedFile.size > 60 * 1024 * 1024) {
      setMensaje("El archivo es demasiado grande. Máximo 60MB.");
      setTipoMensaje("error");
      return;
    }

    setFile(selectedFile);
    setMensaje("");
    
    // Auto-completar título basado en el nombre del archivo
    if (!titulo) {
      const fileName = selectedFile.name.replace('.pdf', '');
      setTitulo(fileName);
    }
  };

  const handleDragOver = (e: DragEvent<HTMLDivElement>) => {
    e.preventDefault();
    setDragOver(true);
  };

  const handleDragLeave = (e: DragEvent<HTMLDivElement>) => {
    e.preventDefault();
    setDragOver(false);
  };

  const handleDrop = (e: DragEvent<HTMLDivElement>) => {
    e.preventDefault();
    setDragOver(false);
    
    const droppedFiles = e.dataTransfer.files;
    if (droppedFiles.length > 0) {
      handleFileSelect(droppedFiles[0]);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!file) {
      setMensaje("Debe seleccionar un archivo PDF.");
      setTipoMensaje("error");
      return;
    }

    if (!titulo.trim()) {
      setMensaje("El título es obligatorio.");
      setTipoMensaje("error");
      return;
    }

    setLoading(true);
    setMensaje("");

    try {
      const formData = new FormData();

      // Metadatos en JSON
      const documentoJson = JSON.stringify({
        titulo: titulo.trim(),
        autor: autor.trim() || "Desconocido",
        editorial: editorial.trim() || "No especificada",
        idioma: idioma.trim() || "Español",
      });

      formData.append("file", file);
      formData.append("documento", documentoJson);

      const response = await fetch("http://localhost:8080/api/documento/upload", {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
        },
        body: formData,
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || "Error al subir documento");
      }

      const result = await response.text();
      setMensaje("Documento subido exitosamente: " + result);
      setTipoMensaje("success");
      
      // Limpiar formulario
      setFile(null);
      setTitulo("");
      setAutor("");
      setEditorial("");
      setIdioma("");
      
      // Limpiar input de archivo
      if (fileInputRef.current) {
        fileInputRef.current.value = '';
      }
      
      // Auto-ocultar mensaje después de 5 segundos
      setTimeout(() => {
        setMensaje("");
      }, 5000);
      
    } catch (error) {
      setMensaje("Error: " + String(error));
      setTipoMensaje("error");
    } finally {
      setLoading(false);
    }
  };

  const removeFile = () => {
    setFile(null);
    if (fileInputRef.current) {
      fileInputRef.current.value = '';
    }
  };

  return (
    <div className="upload-container">
      <div className="upload-card">
        <div className="upload-header">
          <h2 className="upload-title">Subir Documento</h2>
          <p className="upload-subtitle">
            Cargue un archivo PDF y complete la información del documento
          </p>
        </div>

        <form onSubmit={handleSubmit} className="upload-form">
          {/* Zona de drag & drop */}
          <div 
            className={`file-drop-zone ${dragOver ? 'drag-over' : ''} ${file ? 'has-file' : ''}`}
            onDragOver={handleDragOver}
            onDragLeave={handleDragLeave}
            onDrop={handleDrop}
            onClick={() => fileInputRef.current?.click()}
          >
            <input
              ref={fileInputRef}
              type="file"
              className="file-input"
              accept="application/pdf"
              onChange={(e) => handleFileSelect(e.target.files ? e.target.files[0] : null)}
              disabled={loading}
            />
            
            <div className="drop-zone-content">
              <div className="drop-icon">
                {file ? "📄" : "📁"}
              </div>
              <div className="drop-text">
                {file ? "Archivo seleccionado" : "Arrastra tu archivo aquí o haz click para seleccionar"}
              </div>
              <div className="drop-subtext">
                {file ? `${file.name}` : "Solo archivos PDF"}
              </div>
              <div className="file-requirements">
                Tamaño máximo: 60MB
              </div>
            </div>
          </div>

          {/* Preview del archivo */}
          {file && (
            <div className="file-preview">
              <div className="file-icon">📄</div>
              <div className="file-info">
                <div className="file-name">{file.name}</div>
                <div className="file-size">{formatFileSize(file.size)}</div>
              </div>
              <button 
                type="button" 
                className="remove-file"
                onClick={removeFile}
                disabled={loading}
              >
                ✕
              </button>
            </div>
          )}

          {/* Metadatos del documento */}
          <div className="metadata-grid">
            <div className="form-group metadata-group-full">
              <label className="form-label required" htmlFor="titulo">
                Título del Documento
              </label>
              <input
                id="titulo"
                type="text"
                className="form-input"
                placeholder="Ingrese el título del documento"
                value={titulo}
                onChange={(e) => setTitulo(e.target.value)}
                disabled={loading}
                required
              />
            </div>

            <div className="form-group">
              <label className="form-label" htmlFor="autor">
                Autor
              </label>
              <input
                id="autor"
                type="text"
                className="form-input"
                placeholder="Nombre del autor"
                value={autor}
                onChange={(e) => setAutor(e.target.value)}
                disabled={loading}
              />
            </div>

            <div className="form-group">
              <label className="form-label" htmlFor="editorial">
                Editorial
              </label>
              <input
                id="editorial"
                type="text"
                className="form-input"
                placeholder="Casa editorial"
                value={editorial}
                onChange={(e) => setEditorial(e.target.value)}
                disabled={loading}
              />
            </div>

            <div className="form-group">
              <label className="form-label" htmlFor="idioma">
                Idioma
              </label>
              <input
                id="idioma"
                type="text"
                className="form-input"
                placeholder="Idioma del documento"
                value={idioma}
                onChange={(e) => setIdioma(e.target.value)}
                disabled={loading}
              />
            </div>
          </div>

          <button 
            type="submit" 
            className={`upload-button ${loading ? 'loading' : ''}`}
            disabled={loading || !file}
          >
            {loading ? "Subiendo documento..." : "Subir Documento"}
          </button>
        </form>

        {mensaje && (
          <div className={`alert alert-${tipoMensaje}`}>
            {mensaje}
          </div>
        )}
      </div>
    </div>
  );
};

export default SubirDocumento;