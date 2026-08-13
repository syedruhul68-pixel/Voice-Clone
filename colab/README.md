# Colab quick guide for Voice-Clone

This guide explains how to use the Colab cells in `colab/rvc_colab_cells.txt` to perform voice conversion using a free Colab GPU.

1. Open Google Colab
   - Visit: https://colab.research.google.com
   - Create a new notebook.

2. Mount Google Drive
   - Copy the "Cell A" code from `colab/rvc_colab_cells.txt` into the first code cell and run it.
   - When prompted, allow Colab to access your Drive.

3. Upload your WAV files to Drive
   - Using the Google Drive app or web UI, create a folder:
     `MyDrive/VoiceCloneApp/`
   - Upload two files into that folder:
     - `source.wav`  (the audio you want to transform)
     - `reference.wav` (the target voice/example)

4. Install dependencies & clone a conversion repo
   - Copy "Cell B" into the next Colab cell and run it.
   - Note: the repo cloned in the example is a placeholder. If you use a different RVC/so-vits-svc repository, update the clone command.

5. Place model weights
   - The model checkpoints are not included here. Follow the chosen repo's README to obtain the correct model files.
   - Place model checkpoints in `/content/models` within Colab or edit the cell to `wget` them.

6. Run inference
   - Copy the inference command (Cell E) into Colab and run it after models are available.
   - Output files will be written to:
     `/content/drive/MyDrive/VoiceCloneApp/output.wav`
     (and optionally `output.mp3` if you run Cell F)

7. Download the result to your phone
   - In the Drive app, navigate to `MyDrive/VoiceCloneApp/` and download `output.wav` to your phone Downloads folder.
   - Open the Android app (when built) and import/play the resulting file.

Notes & troubleshooting
- Colab free GPU usage is limited by Google (runtime disconnects, session limits). If a cell fails, re-run from the top after reconnecting.
- Model checkpoint links depend on the chosen conversion repository. If you want, I can provide recommended community model links — say "Provide model links".
- If you prefer to skip Colab and run everything locally, tell me and I will give alternate instructions.

That's it — follow the cells in `colab/rvc_colab_cells.txt` in order. When you're ready, say "Next" and I'll give you the next file path and its code.
