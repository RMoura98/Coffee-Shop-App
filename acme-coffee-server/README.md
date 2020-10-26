# Acme Coffee Server

Acme Coffee Server is the REST Web API for the Acme Coffee Android app built using [Node.js](https://nodejs.org/en/).

## Requirements
- [Node.js](https://nodejs.org/en/)
- [Docker](https://www.docker.com/)
- [Docker Compose >= v3.0](https://docs.docker.com/compose/)

## Installation

```bash
npm install
```

## Running the Acme Coffee Server

Start by copying the `default.env` file and renaming it to `.env`.


Run the database container
```bash
docker-compose up -d
```
Start the app
```bash
npm start
```

## Additional notes
If you need to recreate the database from scratch at some point, run:
```bash
npm run reset
```

## Contributing
This project uses the [ESLint](https://eslint.org/) linter so make sure you have it installed.
