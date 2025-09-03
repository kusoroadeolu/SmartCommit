# SmartCommit

SmartCommit is a simple CLI tool that suggests commit messages when you don’t feel like writing them.

---

## 🚀 Setup and Instructions (Windows Only)
Before you start, ensure you have Docker or Docker Desktop properly set up.

### 1. Clone the Repo
```sh
git clone "https://github.com/kusoroadeolu/SmartCommit"
```

### 2. Build the Docker Image
```sh
docker build -t smartcommit .
```

### 3. Configure the `.bat` File (Global Command Access)
1. Create a folder named `bin` anywhere on your device.
2. Place the provided `.bat` file inside the `bin` folder.
3. Open **System Environment Variables** → edit `Path` → add the folder path (not the `.bat` file path).
4. Confirm setup by running:
   ```sh
   where smartcommit
   ```
   If it shows the location of the `.bat` file, you’re set.

> **Note:** The `.bat` script mounts your current directory into Docker so SmartCommit can access your Git repo. Without it, you’d have to type long Docker commands manually.

---

## 📖 Commands

### General Help
- `smartcommit -h` or `smartcommit --help` → Show description and all subcommands.

### Init Command
- `smartcommit --init` → Initialize SmartCommit in a Git repo (creates config file).
- `smartcommit --init -h` → Help for init.
- `smartcommit --init -V` → Show init version.

### Suggest Command
- `smartcommit suggest` → Suggest commit message from staged Git diffs.
- `smartcommit suggest summary` → Suggest shorter, summary-style message.
- `smartcommit suggest detail` → Suggest more detailed message.
- `smartcommit suggest -h` → Help for suggest.
- `smartcommit suggest -V` → Show suggest version.

### Direct Run Command
> ⚠️ Requires a GitHub Personal Access Token (PAT) in the config file for HTTPS auth.

- `smartcommit direct-run` → Runs `git add`, generates commit message, then `git push`.
- `smartcommit direct-run -m summary|detail` → Same, with chosen mode.
- `smartcommit direct-run -m manual -msg "CUSTOM MESSAGE"` → Use custom commit message.
- `smartcommit direct-run -e you@example.com` → Set committer email.
- `smartcommit direct-run -n "Your Name"` → Set committer name.
- `smartcommit direct-run -h` → Help for direct-run.
- `smartcommit direct-run -V` → Show direct-run version.

---

## ⚙️ Config File

SmartCommit requires configuration for API and Git credentials.

- **Gemini API key** → Required. Get one at: [AI Studio](https://aistudio.google.com/apikey)
- **GitHub PAT token** → Needed for direct-run with HTTPS. Generate in GitHub Settings → Developer Settings → PAT.
- **Exclude files** → Add extensions to skip in diffs:
  ```json
  "exclude" : [".md", ".xml"]
  ```  
- **Commit context mode** → Set default to `summary` or `detail`.
- **Person identity** → Set default committer name/email. Defaults to system Git identity if not set.
- **Important** → Add config file to `.gitignore` to avoid leaking API keys.

---

## 🔮 Planned Features

- Global config files (optional).
- SSH-based authentication (instead of PAT).
- More summary/detail context modes.
- Message customization/tweaks.
- GraalVM integration to bypass JVM startup time

---

## 🛠️ Tech Stack

- Java 21
- Docker
- Maven
- JGit & Git

---

## 👤 Author

**Kusoro Victor**  
Version `1.0.0`
