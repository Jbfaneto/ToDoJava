# Use an official MySQL runtime as a parent image
FROM mysql:latest

# Set the environment variables
ENV MYSQL_ROOT_PASSWORD=root
ENV MYSQL_DATABASE=tododb
ENV MYSQL_USER=my_user
ENV MYSQL_PASSWORD=12345678


# Expose the MySQL port
EXPOSE 3306
