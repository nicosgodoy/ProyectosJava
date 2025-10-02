'use client'

import { Pie, PieChart, Tooltip, Cell, ResponsiveContainer } from 'recharts';
import dataJson from "@/app/dashboard/components/data/data.json"

// Definir el tipo para los datos
interface VentaCategoria {
  nombre: string;
  valor: number;
}

const datos: VentaCategoria[] = dataJson.ventasPorCategoria;

const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042', '#8884D8'];

// Etiquetas personalizadas
const renderLabel = (entry: VentaCategoria) => {
  return `${entry.nombre}: ${entry.valor}`;
};

export default function TablaCircular() {
  return (
    <div className="w-full bg-card p-1 rounded-lg border shadow-sm flex flex-col items-center">
      <ResponsiveContainer width="100%" height={380}>
        <PieChart>
          <Pie
            data={datos}
            dataKey="valor"
            nameKey="nombre"
            cx="50%"
            cy="50%"
            outerRadius={75}
            // @ts-expect-error - Recharts label prop tiene tipado complejo
            label={renderLabel}   // 👈 Etiqueta personalizada
          >
            {datos.map((entry, index) => (
              <Cell key={`cell-${index}`} fill={COLORS[index]} />
            ))}
          </Pie>
          <Tooltip 
            contentStyle={{
              backgroundColor: 'hsl(var(--card))',
              border: '1px solid hsl(var(--border))',
              borderRadius: '6px'
            }}
          />
        </PieChart>
      </ResponsiveContainer>
    </div>
  )
}