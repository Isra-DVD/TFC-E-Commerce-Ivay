# Use an official Node.js runtime as a base image (alpine = minimal image size)
FROM node:20-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy only package files first to leverage Docker cache during builds
COPY package.json package-lock.json ./

# Install project dependencies
RUN npm install

# Copy all other source files into the working directory
COPY . .

# Build the React application for production (output goes to /app/build)
RUN npm run build

# Install 'serve' globally to serve the built static files
RUN npm install -g serve

# Define the default command to run the production server on port 3000
CMD ["serve", "-s", "build", "-l", "3000"]

# Expose port 3000 (used by the React app)
EXPOSE 3000