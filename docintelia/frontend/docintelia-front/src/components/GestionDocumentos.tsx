import React, { useEffect, useState } from "react";
import "./GestionDocumentos.css";

type Documento = {
  id_documento: number;
  titulo: string | null;
  autor: string;
  fecha: string;
  editorial: string;
  idioma: string;
  rutaArchivo: string;
};

type EditModal = {
  isOpen: boolean;
  documento: Documento | null;
};

type GestionDocumentosProps = {
  token: string; // JWT
};

const GestionDocumentos: React.FC<GestionDocumentosProps> = ({ token }) => {
  const [documentos, setDocumentos] = useState<Documento[]>([]);
  const [filtro, setFiltro] = useState("");
  const [pagina, setPagina] = useState(1);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [editModal, setEditModal] = useState<EditModal>({ isOpen: false, documento: null });
  const [editForm, setEditForm] = useState({
    titulo: "",
    autor: "",
    editorial: "",
    idioma: ""
  });
  const [saving, setSaving] = useState(false);

  const documentosPorPagina = 9; // 3x3 grid

  useEffect(() => {
    const fetchDocumentos = async () => {
      setLoading(true);
      setError("");
      try {
        const response = await fetch(
          "http://localhost:8080/api/documento/gestion-documentos",
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        if (!response.ok) throw new Error("Error al obtener documentos");

        const data: Documento[] = await response.json();
        setDocumentos(data);
    
      } catch {
        setError("No se pudo cargar la lista de documentos");
      } finally {
        setLoading(false);
      }
    };

    fetchDocumentos();
  }, [token]);

  const eliminarDocumento = async (id: number, titulo: string) => {
    const confirmMessage = `¿Está seguro de que desea eliminar "${titulo || 'Sin título'}"?\n\nEsta acción no se puede deshacer.`;
    
    if (!window.confirm(confirmMessage)) return;

    try {
      const response = await fetch(
        `http://localhost:8080/api/documento/eliminar/${id}`,
        {
          method: "DELETE",
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      if (!response.ok) throw new Error("Error al eliminar documento");

      setDocumentos((prev) =>
        prev.filter((doc) => doc.id_documento !== id)
      );
      
      // Ajustar página si es necesario
      const documentosRestantes = documentos.length - 1;
      const paginasNecesarias = Math.ceil(documentosRestantes / documentosPorPagina);
      if (pagina > paginasNecesarias && paginasNecesarias > 0) {
        setPagina(paginasNecesarias);
      }
      
    } catch {
      alert("No se pudo eliminar el documento. Inténtelo de nuevo.");
    }
  };

  const abrirModalEdicion = (documento: Documento) => {
    setEditForm({
      titulo: documento.titulo || "",
      autor: documento.autor,
      editorial: documento.editorial,
      idioma: documento.idioma
    });
    setEditModal({ isOpen: true, documento });
  };

  const cerrarModal = () => {
    setEditModal({ isOpen: false, documento: null });
    setEditForm({ titulo: "", autor: "", editorial: "", idioma: "" });
  };

  const guardarCambios = async () => {
    if (!editModal.documento) return;
    
    const { titulo, autor, editorial, idioma } = editForm;
    
    // Validaciones básicas
    if (!titulo.trim() || !autor.trim()) {
      alert("El título y el autor son obligatorios.");
      return;
    }

    setSaving(true);
    
    try {
      const fecha = new Date().toISOString().split("T")[0];
      
      const response = await fetch(
        `http://localhost:8080/api/documento/actualizar-metadatos/${editModal.documento.id_documento}`, 
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify({ 
            titulo: titulo.trim(), 
            autor: autor.trim(), 
            editorial: editorial.trim() || "No especificada", 
            idioma: idioma.trim() || "Español", 
            fecha 
          }),
        }
      );

      if (!response.ok) throw new Error("Error al actualizar documento");

      // Actualizar lista local
      setDocumentos((prev) =>
        prev.map((doc) =>
          doc.id_documento === editModal.documento!.id_documento
            ? { ...doc, titulo: titulo.trim(), autor: autor.trim(), editorial: editorial.trim(), idioma: idioma.trim(), fecha }
            : doc
        )
      );

      cerrarModal();
      
    } catch (error) {
      console.error(error);
      alert("No se pudo modificar el documento. Inténtelo de nuevo.");
    } finally {
      setSaving(false);
    }
  };

  // Filtrado dinámico
  const documentosFiltrados = documentos.filter((doc) => {
    const texto = `${doc.titulo ?? ""} ${doc.autor} ${doc.editorial} ${doc.idioma}`.toLowerCase();
    return texto.includes(filtro.toLowerCase());
  });

  // Paginación
  const totalPaginas = Math.ceil(documentosFiltrados.length / documentosPorPagina);
  const inicio = (pagina - 1) * documentosPorPagina;
  const fin = inicio + documentosPorPagina;
  const documentosPagina = documentosFiltrados.slice(inicio, fin);

const formatFecha = (fecha: string) => {
  try {
    // Parsear la fecha como componentes locales sin conversión UTC
    const [year, month, day] = fecha.split('-').map(Number);
    const fechaLocal = new Date(year, month - 1, day); // month es 0-indexado
    
    return fechaLocal.toLocaleDateString('es-ES', {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    });
  } catch {
    return fecha;
  }
};

  if (loading) {
    return (
      <div className="documents-container">
        <div className="loading-container">
          <div className="loading-spinner"></div>
          <p className="loading-text">Cargando documentos...</p>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="documents-container">
        <div className="error-container">
          <p className="error-text">{error}</p>
        </div>
      </div>
    );
  }

  return (
    <div className="documents-container">
      {/* Header con filtro */}
      <div className="documents-header">
        <h2 className="documents-title">Gestión de Documentos</h2>
        <div className="search-section">
          <div className="search-input-wrapper">
            <span className="search-icon">🔍</span>
            <input
              type="text"
              value={filtro}
              onChange={(e) => {
                setFiltro(e.target.value);
                setPagina(1);
              }}
              placeholder="Buscar por título, autor, editorial o idioma..."
              className="search-input"
            />
          </div>
          <div className="search-stats">
            {documentosFiltrados.length} documento{documentosFiltrados.length !== 1 ? 's' : ''} 
            {filtro && ' encontrado' + (documentosFiltrados.length !== 1 ? 's' : '')}
          </div>
        </div>
      </div>

      {/* Grid de documentos */}
      {documentosPagina.length > 0 ? (
        <>
          <div className="documents-grid">
            {documentosPagina.map((doc) => (
              <div key={doc.id_documento} className="document-card">
                <div className="document-header">
                  <div className="document-id">ID: {doc.id_documento}</div>
                  <h3 className="document-title">
                    {doc.titulo || "Sin título"}
                  </h3>
                  <div className="document-meta">
                    <div className="meta-item">
                      <span className="meta-icon">👤</span>
                      <span className="meta-value">{doc.autor}</span>
                    </div>
                    <div className="meta-item">
                      <span className="meta-icon">🏢</span>
                      <span className="meta-value">{doc.editorial}</span>
                    </div>
                    <div className="meta-item">
                      <span className="meta-icon">🌐</span>
                      <span className="meta-value">{doc.idioma}</span>
                    </div>
                  </div>
                  <div className="document-date">
                    Fecha: {formatFecha(doc.fecha)}
                  </div>
                </div>

                <div className="document-actions">
                  <a 
                    href={`/documento/${doc.id_documento}?token=${token}`} 
                    target="_blank" 
                    rel="noreferrer"
                    className="action-btn action-btn-view"
                  >
                    👁️ Ver
                  </a>
                  <button 
                    className="action-btn action-btn-edit"
                    onClick={() => abrirModalEdicion(doc)}
                  >
                    ✏️ Editar
                  </button>
                  <button 
                    className="action-btn action-btn-delete"
                    onClick={() => eliminarDocumento(doc.id_documento, doc.titulo || "")}
                  >
                    🗑️ Eliminar
                  </button>
                </div>
              </div>
            ))}
          </div>

          {/* Paginación */}
          {totalPaginas > 1 && (
            <div className="pagination-container">
              <button
                className="pagination-btn"
                onClick={() => setPagina((p) => Math.max(p - 1, 1))}
                disabled={pagina === 1}
              >
                ← Anterior
              </button>
              <span className="pagination-info">
                Página {pagina} de {totalPaginas}
              </span>
              <button
                className="pagination-btn"
                onClick={() => setPagina((p) => Math.min(p + 1, totalPaginas))}
                disabled={pagina === totalPaginas}
              >
                Siguiente →
              </button>
            </div>
          )}
        </>
      ) : (
        <div className="empty-container">
          <p className="empty-text">
            {filtro ? "No se encontraron documentos con ese filtro" : "No hay documentos disponibles"}
          </p>
        </div>
      )}

      {/* Modal de edición */}
      {editModal.isOpen && (
        <div className="modal-overlay" onClick={cerrarModal}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h3 className="modal-title">Editar Documento</h3>
            </div>
            
            <div className="modal-body">
              <form className="modal-form" onSubmit={(e) => { e.preventDefault(); guardarCambios(); }}>
                <div className="form-group">
                  <label className="form-label" htmlFor="edit-titulo">Título *</label>
                  <input
                    id="edit-titulo"
                    type="text"
                    className="form-input"
                    value={editForm.titulo}
                    onChange={(e) => setEditForm({ ...editForm, titulo: e.target.value })}
                    required
                    disabled={saving}
                  />
                </div>

                <div className="form-group">
                  <label className="form-label" htmlFor="edit-autor">Autor *</label>
                  <input
                    id="edit-autor"
                    type="text"
                    className="form-input"
                    value={editForm.autor}
                    onChange={(e) => setEditForm({ ...editForm, autor: e.target.value })}
                    required
                    disabled={saving}
                  />
                </div>

                <div className="form-group">
                  <label className="form-label" htmlFor="edit-editorial">Editorial</label>
                  <input
                    id="edit-editorial"
                    type="text"
                    className="form-input"
                    value={editForm.editorial}
                    onChange={(e) => setEditForm({ ...editForm, editorial: e.target.value })}
                    placeholder="No especificada"
                    disabled={saving}
                  />
                </div>

                <div className="form-group">
                  <label className="form-label" htmlFor="edit-idioma">Idioma</label>
                  <input
                    id="edit-idioma"
                    type="text"
                    className="form-input"
                    value={editForm.idioma}
                    onChange={(e) => setEditForm({ ...editForm, idioma: e.target.value })}
                    placeholder="Español"
                    disabled={saving}
                  />
                </div>
              </form>
            </div>

            <div className="modal-actions">
              <button 
                className="modal-btn modal-btn-cancel"
                onClick={cerrarModal}
                disabled={saving}
              >
                Cancelar
              </button>
              <button 
                className="modal-btn modal-btn-save"
                onClick={guardarCambios}
                disabled={saving}
              >
                {saving ? "Guardando..." : "Guardar"}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default GestionDocumentos;