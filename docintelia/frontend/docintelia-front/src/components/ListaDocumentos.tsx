// ListaDocumentos.tsx
import React, { useState } from "react";
import "./ListaDocumentos.css";

type ResultadoBusquedaDTO = {
  id: number;
  nombreArchivo: string;
  rutaArchivo: string;
  score: number;
  extracto: string;
  urlDescarga: string;
};

type ListaDocumentosProps = {
  token: string; // JWT
};

const ListaDocumentos: React.FC<ListaDocumentosProps> = ({ token }) => {
  const [consulta, setConsulta] = useState("");
  const [resultados, setResultados] = useState<ResultadoBusquedaDTO[]>([]);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const [pagina, setPagina] = useState(1);

  const resultadosPorPagina = 10;
  const totalPaginas = Math.ceil(resultados.length / resultadosPorPagina);

  const buscarDocumentos = async () => {
    if (!consulta.trim()) return;

    setLoading(true);
    setError("");
    setResultados([]);
    setPagina(1);

    try {
      const response = await fetch(
        `http://localhost:8080/api/documento/buscar?q=${encodeURIComponent(consulta)}`,
        {
          headers: {
            "Authorization": `Bearer ${token}`,
          },
        }
      );

      if (!response.ok) throw new Error("Error al obtener resultados");

      const data: ResultadoBusquedaDTO[] = await response.json();
      setResultados(data);
    } catch {
      setError("No se pudo cargar la lista de documentos");
    } finally {
      setLoading(false);
    }
  };

  const inicio = (pagina - 1) * resultadosPorPagina;
  const fin = inicio + resultadosPorPagina;
  const resultadosPagina = resultados.slice(inicio, fin);

return (
  <div className="search-container">
    <h2>Buscador de Documentos</h2>

    <div className="search-form">
      <input
        type="text"
        value={consulta}
        onChange={(e) => setConsulta(e.target.value)}
        placeholder="Ingrese término de búsqueda..."
        className="search-input"
      />
      <button onClick={buscarDocumentos} className="search-button">
        Buscar
      </button>
    </div>

    {loading && <p className="status-message">Cargando...</p>}
    {error && <p className="status-message error">{error}</p>}

    {resultadosPagina.length > 0 && (
      <>
        <ul className="search-results">
          {resultadosPagina.map((r) => (
            <li key={r.id} className="result-item">
              <a href={`/documento/${r.id}`} className="result-link">
                <span className="result-title">{r.nombreArchivo}</span>
              </a>
              <p className="result-url">
                {`http://localhost:8080/api/documento/${r.id}`}
              </p>
              <p className="result-snippet">{r.extracto}</p>
              <p className="result-score">Puntaje: {r.score.toFixed(2)}</p>
            </li>
          ))}
        </ul>
        <div className="pagination-container">
          <button
            onClick={() => setPagina((p) => Math.max(p - 1, 1))}
            disabled={pagina === 1}
            className="pagination-button"
          >
            Anterior
          </button>
          <span className="pagination-info">
            Página {pagina} de {totalPaginas}
          </span>
          <button
            onClick={() => setPagina((p) => Math.min(p + 1, totalPaginas))}
            disabled={pagina === totalPaginas}
            className="pagination-button"
          >
            Siguiente
          </button>
        </div>
      </>
    )}

    {!loading && resultados.length === 0 && consulta && !error && (
      <p className="status-message">No se encontraron resultados.</p>
    )}
  </div>
);


};

export default ListaDocumentos;
