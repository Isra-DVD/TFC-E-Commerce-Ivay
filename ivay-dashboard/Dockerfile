# Use an official Node.js 20 runtime with Alpine Linux for minimal image size
FROM node:20-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy dependency definition files first to take advantage of Docker layer caching
COPY package.json package-lock.json ./

# Install project dependencies
RUN npm install

# Copy the remaining source code into the container
COPY . .

# Build the production-ready version of the React application
RUN npm run build

# Install the 'serve' package globally to serve the built static files
RUN npm install -g serve

# Default command to start the server and serve the 'build' directory on port 3000
CMD ["serve", "-s", "build", "-l", "3000"]

# Expose port 3000 to allow external access
EXPOSE 3000
