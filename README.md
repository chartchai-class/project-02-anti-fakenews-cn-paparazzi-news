# Truth Moment - Anti Fake News Platform

A comprehensive fake news detection and verification platform designed to help users identify and combat misinformation.

## Online Demo

The project is deployed on Vercel. You can access it through the following link:
[https://project-01-anti-fakenews-cn-paparaz-sigma.vercel.app/]

## Team Members

### Member 1
- **Name:** Shen Xinyan
- **ID:** 20232090
- **Email:** 3930098362@qq.com

### Member 2
- **Name:** Wu Runxin
- **ID:**20232083
- **Email:** 3282982622@qq.com

### Member 3
- **Name:** Zhao Meiling
- **ID:**20232089
- **Email:** 2804973446@qq.com

## Overview 项目概述
A comprehensive fake news detection and verification platform designed to help users identify and combat misinformation. The system leverages community voting, expert analysis, and media literacy education to promote information accuracy.


## Features 功能特性

### Core Functionalities 核心功能
- **News Aggregation**: Curated news content from various sources with credibility ratings
- **Community Trust Assessment**: User voting system to evaluate news trustworthiness
- **Comment System**: Engage in discussions with trust ratings for each comment
- **Trust Index**: Visual representation of news and source credibility
- **Media Literacy Education**: Resources to help users identify fake news

### Technical Highlights 技术亮点
- **Frontend**: Modern React-based interface with responsive design
- **Backend**: RESTful API architecture with secure data handling
- **Real-time Interactions**: Dynamic updates for votes and comments
- **Accessibility Support**: WCAG-compliant UI components

## Quick Start

### Prerequisites
- Install Docker and Docker Compose
- JDK 17+
- Node.js 18+

### Start the Project with Docker Compose

```bash
docker-compose up -d --build
```

After services are started, you can access them at:
- Frontend Application: http://localhost:3000
- Backend API: http://localhost:8080
- MySQL Database: localhost:3306 (internal access only)

### Default User Accounts

The system creates the following default accounts upon initialization:

- **Administrator**
  - Username: admin
  - Password: admin123

- **Fact Checker**
  - Username: factchecker
  - Password: admin123

- **Regular User**
  - Username: user
  - Password: admin123

### Local Development Setup

#### Frontend Development
```bash
# Navigate to frontend directory
cd frontend

# Install dependencies
npm install

# Start development server
npm run dev
```

#### Backend Development
```bash
# Navigate to backend directory
cd backend

# Start Spring Boot application
./mvnw spring-boot:run
```

## Usage 使用说明

1. **Browse News**: Visit the homepage to see the latest news articles with trust scores
2. **Evaluate Trustworthiness**: Vote on whether you find news articles trustworthy or not
3. **Join Discussions**: Leave comments and rate others' comments based on trustworthiness
4. **Learn Media Literacy**: Access educational resources to improve your fake news detection skills

## Project Structure

```
project-02-anti-fakenews-cn-paparazzi-news/
├── backend/              # Spring Boot backend application
│   ├── src/              # Source code
│   ├── pom.xml           # Maven configuration file
│   └── Dockerfile        # Docker configuration file
├── frontend/             # React frontend application
│   ├── src/              # React components and logic
│   │   ├── components/   # Reusable UI components
│   │   ├── pages/        # Page components
│   │   ├── api/          # API interaction tools
│   │   └── style.css     # Global styles
│   ├── package.json      # Frontend dependencies
│   ├── Dockerfile        # Docker configuration file
│   └── nginx.conf        # Nginx configuration file
├── mysql-init/           # MySQL database initialization scripts
└── docker-compose.yml    # Docker Compose configuration file
```

## Technology Stack

### Frontend
- **React 19**: Frontend JavaScript framework
- **React Router 7**: Frontend routing management
- **Vite**: Build tool
- **Docker**: Containerization deployment

### Backend
- Spring Boot 3.x
- Spring Security + JWT
- Spring Data JPA
- MySQL
- Docker

## Contributing 贡献指南

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License 许可证

This project is licensed under the MIT License - see the LICENSE file for details.

## Contact 联系方式

- Email: hhibo2778@gmail.com
- Project Link: [Truth Moment]https://github.com/chartchai-class/project-01-anti-fakenews-cn-paparazzi-news


© 2025 Truth Moment - Anti Fake News Platform. All rights reserved.