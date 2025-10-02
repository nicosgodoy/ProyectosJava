'use client';

import React from 'react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import data from "@/app/dashboard/components/data/data.json"
const dataJson = data.rendimientoProductos;


const ProductPerformanceChart = () => {
  return (
    <div style={{ width: '100%', height: '400px' }}>
      
      <ResponsiveContainer width="100%" height="100%">
        <BarChart
          data={dataJson}
          margin={{
            top: 20,
            right: 30,
            left: 20,
            bottom: 5,
          }}
        >
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="nombre" />
          <YAxis />
          <Tooltip />
          <Legend />
          
          {/* Una barra sola para Retención */}
          <Bar dataKey="Retención" fill="#8884d8" />

          {/* Ingresos y Ganancia apilados en la misma barra */}
          <Bar dataKey="Ingresos" stackId="a" fill="#82ca9d" />
          <Bar dataKey="Ganancia" stackId="a" fill="#ffc658" />
        </BarChart>
      </ResponsiveContainer>
    </div>
  );
};

export default ProductPerformanceChart;
