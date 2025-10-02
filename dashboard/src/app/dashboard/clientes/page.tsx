'use client'

import { useState } from "react"
import {
  Table,
  TableBody,
  TableCaption,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"
import { Button } from "@/components/ui/button"
import { Pencil, Trash2 } from "lucide-react"
import data from "@/app/dashboard/components/data/data.json"
import Image from "next/image"

// Definir el tipo para cliente
interface Cliente {
  id: number;
  nombre: string;
  correo: string;
  telefono: string;
  pais: string;
  imagen: string;
}

export default function Page() {
  const [clientesEliminados, setClientesEliminados] = useState<number[]>([]);
  const clientes: Cliente[] = data.clientes;

  // Filtrar clientes no eliminados
  const clientesVisibles = clientes.filter(cliente => 
    !clientesEliminados.includes(cliente.id)
  );

  const handleEdit = (cliente: Cliente) => {
    // Simular edición
    alert(`Editando cliente: ${cliente.nombre}`);
  };

  const handleDelete = (cliente: Cliente) => {
    if (window.confirm(`¿Estás seguro de eliminar a ${cliente.nombre}?`)) {
      // Solo agregar ID a la lista de eliminados
      setClientesEliminados(prev => [...prev, cliente.id]);
    }
  };

  return (
    <div >
      <h1 className="text-center font-extrabold text-4xl mb-8">Clientes</h1>
      <Table>
        <TableCaption>Mostrando {clientesVisibles.length} de {clientes.length} clientes</TableCaption>
        <TableHeader>
          <TableRow className="text-center justify-center bg-blue-600">
            <TableHead className="w-[100px] text-left text-black">Id</TableHead>
            <TableHead className="text-left text-black">Nombre</TableHead>
            <TableHead className="text-left text-black">Correo</TableHead>
            <TableHead className="text-left text-black">Telefono</TableHead>
            <TableHead className="text-center text-black">Pais</TableHead>
            <TableHead className="text-center text-black">Imagen</TableHead>
            <TableHead className="text-center text-black">Acciones</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {clientesVisibles.map((cliente) => (
            <TableRow key={cliente.id}>
              <TableCell className="font-medium  bg-blue-200">{cliente.id}</TableCell>
              <TableCell className=" bg-blue-200">{cliente.nombre}</TableCell>
              <TableCell className=" bg-blue-200">{cliente.correo}</TableCell>
              <TableCell className="text-left  bg-blue-200">{cliente.telefono}</TableCell>
              <TableCell className="text-center  bg-blue-200">{cliente.pais}</TableCell>
              <TableCell className="text-center  bg-blue-200">  
                <Image
                  src={cliente.imagen}
                  alt={cliente.nombre}
                  width={85}
                  height={85}
                  className="rounded-full object-cover mx-auto"
                />
              </TableCell>
              <TableCell className="text-center  bg-blue-200">
                <div className="flex justify-center gap-2">
                  <Button
                    variant="outline"
                    size="sm"
                    onClick={() => handleEdit(cliente)}
                    className="hover:bg-blue-50 hover:border-blue-300"
                  >
                    <Pencil className="h-4 w-4" />
                  </Button>
                  <Button
                    variant="outline"
                    size="sm"
                    onClick={() => handleDelete(cliente)}
                    className="hover:bg-red-50 hover:border-red-300 hover:text-red-600"
                  >
                    <Trash2 className="h-4 w-4" />
                  </Button>
                </div>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  )
}