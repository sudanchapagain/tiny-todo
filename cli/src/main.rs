mod cli;
mod todo;
mod file;

use anyhow::Result;
use clap::Parser;
use crate::cli::{Cli, Commands};

fn main() -> Result<()> {
    let cli = Cli::parse();

    match cli.command {
        Commands::Add { title } => {
            println!("{}", title);
        }
        Commands::List => {
            println!("");
        }
        Commands::Delete { id } => {
            println!("{}", id);
        }
        Commands::Update { id, title } => {
            println!("{}{}", id, title);
        }
    }

    Ok(())
}
