"use client"

import data from "@/app/dashboard/components/data/data.json"
import {
  Table,
  TableBody,
  TableCaption,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"
import { Badge } from "@/components/ui/badge"
import { cn } from "@/lib/utils" 

// Mapeo de estados a clases de color (basado en las variantes de Badge)
const estadoColores = {
  Entregado: "border-transparent bg-emerald-500 text-white shadow hover:bg-emerald-600",
  Procesado: "border-transparent bg-secondary text-secondary-foreground hover:bg-secondary/80",
  Pendiente: "border-amber-500 text-amber-700 bg-amber-50 hover:bg-amber-100",
  Cancelado:  "border-transparent bg-destructive text-destructive-foreground shadow hover:bg-destructive/80",
} as const;

export default function Page() {
  const pedidos = data.pedidos

  // Función para contar por estado
  const contarPorEstado = (estado: string) =>
    pedidos.filter((p) => p.estado === estado).length

  const estados = ["Entregado", "Procesado", "Pendiente", "Cancelado"]

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-center font-extrabold text-4xl mb-8"> Órdenes de compra</h1>
      
      {/* Grid de InfoCards con colores consistentes con los badges */}
      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-4 mb-8">
        {estados.map((estado) => {
          const colorClase = estadoColores[estado as keyof typeof estadoColores];
          
          return (
            <div key={estado} className={cn("rounded-lg border-2 p-4 text-center", colorClase)}>
              <h3 className="font-semibold mb-2">{estado}</h3>
              <p className="text-2xl font-bold">{contarPorEstado(estado)}</p>
            </div>
          );
        })}
      </div>
      
      {/* Tabla de pedidos */}
      <div className="rounded-md border">
        <Table className="bg-blue-200">
          <TableCaption></TableCaption>
          <TableHeader>
            <TableRow className="bg-blue-600 ">
              <TableHead className="w-[100px] text-center text-black">Id</TableHead>
              <TableHead className="text-left text-black">Cliente</TableHead>
              <TableHead className="text-left text-black">Correo</TableHead>
              <TableHead className="text-left text-black">Estado</TableHead>
              <TableHead className="text-center text-black">Fecha</TableHead>
              <TableHead className="text-center text-black">País</TableHead>
              <TableHead className="text-center text-black">Total</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {pedidos.map((datos) => (
              <TableRow key={datos.id}>
                <TableCell className="font-medium text-center">{datos.id}</TableCell>
                <TableCell>{datos.cliente}</TableCell>
                <TableCell>{datos.correo}</TableCell>
                <TableCell className="text-left">
                  <Badge
                    variant={
                      ({
                        Entregado: "success",
                        Procesado: "secondary",
                        Pendiente: "outline",
                        Cancelado: "destructive",
                      } as const)[datos.estado] ?? "default"
                    }
                    className="capitalize"
                  >
                    {datos.estado}
                  </Badge>
                </TableCell>
                <TableCell className="text-center">{datos.fecha}</TableCell>
                <TableCell className="text-center">{datos.pais}</TableCell>
                <TableCell className="text-center">${datos.total}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </div>
    </div>
  )
}