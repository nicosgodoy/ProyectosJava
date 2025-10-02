// src/components/SearchBar.tsx
import React from "react";
import { Search } from "lucide-react";
import "./SearchBar.css";

interface SearchBarProps {
  value: string;
  onChange: (value: string) => void;
  placeholder?: string;
}

const SearchBar: React.FC<SearchBarProps> = ({ value, onChange, placeholder }) => {
  return (
    <div className="search-bar">
      <Search className="search-icon" size={18} />
      <input
        type="text"
        className="search-input"
        placeholder={placeholder || "Buscar..."}
        value={value}
        onChange={(e) => onChange(e.target.value)}
      />
    </div>
  );
};

export default SearchBar;
