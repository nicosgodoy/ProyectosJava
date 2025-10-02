"use client"

import React from "react"
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from "recharts"
import dataJson from "@/app/dashboard/components/data/data.json"

type Venta = {
  mes: string
  ventas: number
}

const data: Venta[] = dataJson.ventasMensuales

// Props del label personalizado
type LabelProps = {
  x?: number
  y?: number
  stroke?: string
  value?: string | number
}

// Props del tick personalizado
type TickProps = {
  x?: number
  y?: number
  stroke?: string
  payload?: { value: string | number }
}

const CustomizedLabel: React.FC<LabelProps> = ({ x, y, stroke, value }) => (
  <text x={x} y={y} dy={-4} fill={stroke} fontSize={10} textAnchor="middle">
    {value}
  </text>
)

const CustomizedAxisTick: React.FC<TickProps> = ({ x, y, payload }) => (
  <g transform={`translate(${x},${y})`}>
    <text
      x={0}
      y={0}
      dy={16}
      textAnchor="end"
      fill="#666"
      transform="rotate(-35)"
    >
      {payload?.value}
    </text>
  </g>
)

export default function ExampleChart() {
  return (
    <div className="w-full h-96">
      <ResponsiveContainer width="100%" height="100%">
        <LineChart
          data={data}
          margin={{ top: 20, right: 30, left: 20, bottom: 10 }}
        >
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="mes" height={60} tick={<CustomizedAxisTick />} />
          <YAxis />
          <Tooltip />
          <Legend />
          <Line
            type="monotone"
            dataKey="ventas"
            stroke="#8884d8"
            label={<CustomizedLabel />}
          />
        </LineChart>
      </ResponsiveContainer>
    </div>
  )
}
