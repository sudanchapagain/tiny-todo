package main

import (
	"log"
	"os"

	"github.com/sudanchapagain/tinytodocli/v2/cmd"

	"github.com/urfave/cli/v2"
)

func main() {
	const appVersion = "4.0"

	app := &cli.App{
		Name:    "tiny todo cli",
		Usage:   "a simple todo list manager",
		Version: appVersion,
		Commands: []*cli.Command{
			{
				Name:    "add",
				Aliases: []string{"a"},
				Usage:   "Add a new entry to todo list.",
				Action: func(c *cli.Context) error {
					cmd.AddNewItem()
					return nil
				},
			},
		},
	}

	err := app.Run(os.Args)
	if err != nil {
		log.Fatal(err)
	}
}
