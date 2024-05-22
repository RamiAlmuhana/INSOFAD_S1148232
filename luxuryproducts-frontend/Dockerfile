# Gebruik een basis image met een nieuwere versie van Node.js
FROM node:21 AS build

# Stel een werkdirectory in
WORKDIR /app

# Kopieer package.json en package-lock.json naar de werkdirectory
COPY package*.json ./

# Installeer de afhankelijkheden
RUN npm install

# Installeer de Angular CLI globaal
RUN npm install -g @angular/cli

# Kopieer de rest van de applicatiebestanden naar de werkdirectory
COPY . /app

# Bouw de Angular-applicatie
RUN npm run build --prod

# Gebruik een basis image met nginx voor de webserver
FROM nginx:stable-alpine

RUN apk add nano

# Verwijder de standaard configuratie van nginx
RUN rm -rf /usr/share/nginx/html/*

# Kopieer de gecompileerde Angular-app naar de nginx directory
COPY --from=build /app/dist/infirfs-auth-project/browser /usr/share/nginx/html

# Kopieer de aangepaste nginx configuratie naar de container
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Exposeer de standaard nginx poort
EXPOSE 80

# Start nginx wanneer de container start
CMD ["nginx", "-g", "daemon off;"]
