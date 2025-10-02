"use client";
import React from "react";
import Link from "next/link";

const links = [
  { name: "productos", href: "productos" },
  { name: "clientes", href: "clientes" },
  { name: "ventas", href:"ventas"},
  { name: "ordenes de compras", href:"ordenes-de-compras"},

];

export default function Sidebar() {
  return (
    <aside
      id="sidebar"
      className="fixed hidden z-20 h-full top-0 left-0 pt-16 lg:flex flex-shrink-0 flex-col w-64 transition-width duration-75"
      aria-label="Sidebar"
    >
      <div className="relative flex-1 flex flex-col min-h-0 borderR border-gray-200 bg-[#0a2748] pt-0">
        <div className="flex-1 flex flex-col pt-5 pb-4 overflow-y-auto">
          <div className="flex-1 px-3 bg-[#0a2748] divide-y space-y-1">
            <ul className="space-y-2 pb-2 ">
              {links.map((link) => (
                <li key={link.href}>
                  <Link
                    href={link.href}
                    className="text-base capitalize text-gray-900 font-normal rounded-lg flex items-center p-2 hover:bg-orange-400 group"
                  >
                    <span className="ml-3 text-white">{link.name}</span>
                  </Link>
                </li>
              ))}
            </ul>
          </div>
        </div>
      </div>
    </aside>
  );
}
