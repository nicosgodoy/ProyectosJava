"use client";
import React from "react";
import Navbar from "@/app/dashboard/components/layout/Navbar";
import Sidebar from "@/app/dashboard/components/layout/SideBar";
import MainContent from "@/app/dashboard/components/layout/MainContent";
import Footer from "@/app/dashboard/components/layout/Footer";

export default function DashboardLayout({ children }: { children: React.ReactNode }) {
  return (
    <>
      <Navbar />
      <div className="flex overflow-hidden bg-white pt-16">
        <Sidebar />
        <MainContent>{children}</MainContent>
      </div>
      <Footer />
    </>
  );
}
