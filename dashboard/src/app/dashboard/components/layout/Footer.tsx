"use client";
import React from "react";
import { FaLinkedin } from "react-icons/fa";

export default function Footer() {
  return (
    <footer className="bg-white md:flex md:items-center md:justify-between shadow rounded-lg p-4 md:p-6 xl:p-8 my-6 mx-4">
  <div className="flex items-center justify-center gap-2 w-full">
    <span className="text-sm text-gray-500">Enlaces personales:</span>
    <a
      href="https://www.linkedin.com/in/nicolas-sebastian-godoy-03418111b/"
      target="_blank"
      rel="noopener noreferrer"
      aria-label="LinkedIn de Nicolás Godoy"
      className="hover:scale-110 transition-transform"
    >
      <FaLinkedin className="text-[#0A66C2] w-8 h-8" />
    </a>
  </div>
</footer>
  );
}
