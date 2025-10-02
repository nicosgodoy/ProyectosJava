import {
  Table,
  TableBody,
  TableCaption,
  TableCell,
 
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"
import data from "@/app/dashboard/components/data/data.json"
import Image from "next/image"


export default function Page() {

 const productos = data.productos

  return (
    <div>
      <h1 className="text-center font-extrabold text-4xl mb-8">Productos disponibles</h1>
      <div>
      <Table>
      <TableCaption></TableCaption>
      <TableHeader>
        <TableRow className="text-center justify-center bg-blue-600 ">
          <TableHead className="w-[100px] text-center text-black">Id</TableHead>
          <TableHead className="text-center text-black ">Nombre</TableHead>
          <TableHead className="text-left text-black">Categoria</TableHead>
          <TableHead className="text-left text-black">Stock</TableHead>
          <TableHead className="text-center text-black">Ventas</TableHead>
          <TableHead className="text-center text-black">Precio $</TableHead>
          <TableHead className="text-center text-black">Imagen</TableHead>
        </TableRow>
      </TableHeader>
      <TableBody>
        {productos.map((datos) => (
          <TableRow key={datos.id} className="bg-blue-200">
            <TableCell className="font-medium bg-blue-200">{datos.id}</TableCell>
            <TableCell className="text-center bg-blue-200">{datos.nombre}</TableCell>
            <TableCell className="bg-blue-200">{datos.categoria}</TableCell>
            <TableCell className="text-left bg-blue-200">{datos.stock}</TableCell>
            <TableCell className="text-center bg-blue-200">{datos.ventas}</TableCell>
            <TableCell className="text-center bg-blue-200">{datos.precio}</TableCell>
            <TableCell className="text-center bg-blue-200"><Image
          src={datos.imagen}
          alt={datos.nombre}
          width={85}
          height={85}
          className="rounded-full object-cover mx-auto"
        /></TableCell>

          </TableRow>
        ))}
      </TableBody>
      
    </Table>
    </div>
    </div>
  )
}
