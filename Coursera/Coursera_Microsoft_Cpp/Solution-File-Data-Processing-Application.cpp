/**
 * Data Processing Application
 * C++ Programming Fundamentals Capstone Project
 *
 * This program reads data from files (CSV, JSON, or plain text),
 * processes it through various functions, and outputs the results.
 */

#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <algorithm>
#include <stdexcept>
#include <sstream>
#include <map>
#include <iomanip>
#include <numeric>
#include <regex>
#include <cmath>

// Include the nlohmann/json library
#include "json.hpp"
using json = nlohmann::json;

namespace FileIO {
    std::string readFile(const std::string& filename) {
        std::ifstream file(filename);
        if (!file.is_open()) {
            throw std::runtime_error("Could not open file: " + filename);
        }
        std::stringstream buffer;
        buffer << file.rdbuf();
        if (file.bad()) {
            throw std::runtime_error("Error reading from file: " + filename);
        }
        file.close();
        return buffer.str();
    }
    
    void writeFile(const std::string& filename, const std::string& data) {
        std::ofstream file(filename);
        if (!file.is_open()) {
            throw std::runtime_error("Could not open file for writing: " + filename);
        }
        file << data;
        if (file.bad()) {
            throw std::runtime_error("Error writing to file: " + filename);
        }
        file.close();
    }
    
    bool fileExists(const std::string& filename) {
        std::ifstream file(filename);
        return file.good();
    }
    
    std::string getFileExtension(const std::string& filename) {
        size_t dotPos = filename.find_last_of(".");
        if (dotPos != std::string::npos) {
            return filename.substr(dotPos + 1);
        }
        return "";
    }
}

namespace DataParsing {
    struct DataRecord {
        std::string id;
        std::string name;
        int age;
        std::string email;
        std::string country;
        double score;
        DataRecord() : age(0), score(0.0) {}
    };
    
    std::vector<DataRecord> parseCsvData(const std::string& fileContent) {
        std::vector<DataRecord> records;
        std::istringstream stream(fileContent);
        std::string line;
        
        if (!std::getline(stream, line)) {
            throw std::runtime_error("CSV file is empty or has no header");
        }
        
        std::vector<std::string> headers;
        std::istringstream headerStream(line);
        std::string header;
        while (std::getline(headerStream, header, ',')) {
            header.erase(0, header.find_first_not_of(" \t"));
            header.erase(header.find_last_not_of(" \t") + 1);
            headers.push_back(header);
        }
        
        std::map<std::string, int> columnMap;
        for (size_t i = 0; i < headers.size(); i++) {
            columnMap[headers[i]] = static_cast<int>(i);
        }
        
        while (std::getline(stream, line)) {
            std::vector<std::string> values;
            std::istringstream rowStream(line);
            std::string value;
            while (std::getline(rowStream, value, ',')) {
                value.erase(0, value.find_first_not_of(" \t"));
                value.erase(value.find_last_not_of(" \t") + 1);
                values.push_back(value);
            }
            
            if (values.size() < headers.size()) {
                continue;
            }
            
            DataRecord record;
            if (columnMap.count("id")) record.id = values[columnMap["id"]];
            if (columnMap.count("name")) record.name = values[columnMap["name"]];
            if (columnMap.count("age")) {
                try { record.age = std::stoi(values[columnMap["age"]]); }
                catch (...) { record.age = 0; }
            }
            if (columnMap.count("email")) record.email = values[columnMap["email"]];
            if (columnMap.count("country")) record.country = values[columnMap["country"]];
            if (columnMap.count("score")) {
                try { record.score = std::stod(values[columnMap["score"]]); }
                catch (...) { record.score = 0.0; }
            }
            records.push_back(record);
        }
        return records;
    }
    
    std::vector<DataRecord> parseData(const std::string& fileContent, const std::string& fileType) {
        if (fileType == "csv") {
            return parseCsvData(fileContent);
        } else {
            throw std::runtime_error("Unsupported file format: " + fileType);
        }
    }
    
    bool validateRecord(const DataRecord& record) {
        if (record.id.empty() || record.name.empty()) return false;
        if (record.age < 0 || record.age > 120) return false;
        if (!record.email.empty()) {
            std::regex emailPattern(R"([a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,})");
            if (!std::regex_match(record.email, emailPattern)) return false;
        }
        if (record.score < 0 || record.score > 100) return false;
        return true;
    }
    
    std::vector<DataRecord> filterValidRecords(const std::vector<DataRecord>& records) {
        std::vector<DataRecord> valid;
        for (const auto& rec : records) {
            if (validateRecord(rec)) valid.push_back(rec);
        }
        return valid;
    }
}
