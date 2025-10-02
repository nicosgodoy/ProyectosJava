import TablaLineal from "@/app/dashboard/components/TablaLineal"
import TablaCircular from "@/app/dashboard/components/TablaCircular"
import TablaBarra from "../components/TablaBarras"

export default function Page() {
  return (
    <div className="p-4 bg-blue-200">
      <div className="grid grid-cols-3 gap-6 bg-blue-200">
        {/* Columna izquierda (2/3 del ancho) */}
        <div className="col-span-2 space-y-6 bg-blue-200">
          <div>
            <h1 className="text-xl font-bold mb-4 text-center bg-blue-200">Ventas Mensuales</h1>
            <TablaLineal />
          </div>
          <div>
            <h1 className="text-xl font-bold mb-4 text-center bg-blue-200">Rendimientos por producto</h1>
            <TablaBarra />
          </div>
        </div>
        
        {/* Columna derecha (1/3 del ancho) */}
        <div>
          <h1 className="text-xl font-bold mb-4 text-center">Ventas por categoría</h1>
          <TablaCircular />
        </div>
      </div>
    </div>
  )
}


