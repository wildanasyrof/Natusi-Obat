package main

import (
	"fmt"

	"github.com/gofiber/fiber/v2"
)

type Obat struct {
	ID       int    `json:"id"`
	Nama     string `json:"nama"`
	Harga    int    `json:"harga"`
	Gambar   string `json:"gambar"`
	Stok     int    `json:"stok"`
	Kategori string `json:"kategori"`
}

type Admin struct {
	Username string `json:"username"`
	Password string `json:"password"`
}

var obats []Obat
var nextID = 1
var admin = Admin{Username: "petugas", Password: "petugas"}

func initData() {
	obats = []Obat{
		{ID: nextID, Nama: "Paracetamol", Harga: 5000, Gambar: "https://example.com/paracetamol.jpg", Stok: 100, Kategori: "Analgesik"},
		{ID: nextID + 1, Nama: "Amoxicillin", Harga: 15000, Gambar: "https://example.com/amoxicillin.jpg", Stok: 50, Kategori: "Antibiotik"},
	}
	nextID += len(obats)
}

func authenticate(c *fiber.Ctx) error {
	var input Admin
	if err := c.BodyParser(&input); err != nil {
		return c.Status(400).JSON(fiber.Map{"status": "error", "message": "Invalid input", "error": err.Error()})
	}
	if input.Username == admin.Username && input.Password == admin.Password {
		return c.JSON(fiber.Map{"status": "success", "message": "Login successful"})
	}
	return c.Status(401).JSON(fiber.Map{"status": "error", "message": "Incorrect Username or Password!"})
}

func getObats(c *fiber.Ctx) error {
	return c.JSON(fiber.Map{"status": "success", "message": "Obat list retrieved", "data": obats})
}

func getObat(c *fiber.Ctx) error {
	id := c.Params("id")
	for _, obat := range obats {
		if fmt.Sprintf("%d", obat.ID) == id {
			return c.JSON(fiber.Map{"status": "success", "message": "Obat found", "data": obat})
		}
	}
	return c.Status(404).JSON(fiber.Map{"status": "error", "message": "Obat not found"})
}

func createObat(c *fiber.Ctx) error {
	var obat Obat
	if err := c.BodyParser(&obat); err != nil {
		return c.Status(400).JSON(fiber.Map{"status": "error", "message": "Invalid input", "error": err.Error()})
	}
	obat.ID = nextID
	nextID++
	obats = append(obats, obat)
	return c.JSON(fiber.Map{"status": "success", "message": "Obat created", "data": obat})
}

func updateObat(c *fiber.Ctx) error {
	id := c.Params("id")
	var updatedObat Obat
	if err := c.BodyParser(&updatedObat); err != nil {
		return c.Status(400).JSON(fiber.Map{"status": "error", "message": "Invalid input", "error": err.Error()})
	}
	for i, obat := range obats {
		if fmt.Sprintf("%d", obat.ID) == id {
			obats[i] = updatedObat
			obats[i].ID = obat.ID
			return c.JSON(fiber.Map{"status": "success", "message": "Obat updated", "data": obats[i]})
		}
	}
	return c.Status(404).JSON(fiber.Map{"status": "error", "message": "Obat not found"})
}

func deleteObat(c *fiber.Ctx) error {
	id := c.Params("id")
	for i, obat := range obats {
		if fmt.Sprintf("%d", obat.ID) == id {
			obats = append(obats[:i], obats[i+1:]...)
			return c.JSON(fiber.Map{"status": "success", "message": "Obat deleted successfully"})
		}
	}
	return c.Status(404).JSON(fiber.Map{"status": "error", "message": "Obat not found"})
}

func main() {
	app := fiber.New()
	initData()

	app.Post("/admin/login", authenticate)
	app.Get("/obats", getObats)
	app.Get("/obats/:id", getObat)
	app.Post("/obats", createObat)
	app.Put("/obats/:id", updateObat)
	app.Delete("/obats/:id", deleteObat)

	app.Listen(":3000")
}
