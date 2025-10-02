"use client";
import React from "react";

export default function MainContent({ children }: { children: React.ReactNode }) {
  return (
    <div
      id="main-content"
      className="h-full w-full relative overflow-y-auto lg:ml-64  bg-gray-50"
    >
      <main>
        <div className="pt-6 px-4">
          <div className="w-full min-h-[calc(100vh-230px)]">
            <div className="bg-white shadow rounded-lg p-0 sm:p-0 xl:p-0">{children}</div>
          </div>
        </div>
      </main>
    </div>
  );
}
