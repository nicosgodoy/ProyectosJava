"use client";
import React from "react";
import Image from "next/image";

export default function Navbar() {
  return (
    <nav className="bg-[#0a2748] border-b border-gray-200 fixed z-30 w-full">
      <div className="px-3 py-3 lg:px-5 lg:pl-3">
        <div className="flex items-center justify-between">
          <div className="flex items-center justify-start">
            <button
              id="toggleSidebarMobile"
              aria-expanded="true"
              aria-controls="sidebar"
              className="lg:hidden mr-2 text-gray-600 hover:text-gray-900 cursor-pointer p-2 hover:bg-gray-100 focus:bg-gray-100 focus:ring-2 focus:ring-gray-100 rounded"
            >
        
            </button>
            <a href="#" className="text-xl font-bold flex items-center lg:ml-2.5">
              <span className="self-center whitespace-nowrap ml-8 text-white text-2xl">Dashboard</span>
            </a>
          </div>
          <div className="flex items-center">
            <p className="mr-10 text-white text-[20px]"> Nicolas Godoy</p>
            <div className="bg-blue-500 text-white p-2 rounded-full w-12 h-12 flex items-center justify-center">
              <Image
        src="/images/admin.jpg"
        alt="Admin"
        width={120}
        height={120}
        className="rounded-full object-cover"
      />
            </div>
          </div>
        </div>
      </div>
    </nav>
  );
}
