import os
import tkinter as tk
from tkinter import filedialog, messagebox
import numpy as np

def compress_lzw(uncompressed):
    dict_size = 256
    dictionary = {bytes([i]): i for i in range(dict_size)}
    w = b""
    result = []
    for c in uncompressed:
        wc = w + bytes([c])
        if wc in dictionary:
            w = wc
        else:
            result.append(dictionary[w])
            dictionary[wc] = dict_size
            dict_size += 1
            w = bytes([c])
    if w:
        result.append(dictionary[w])
    return result

def decompress_lzw(compressed):
    dict_size = 256
    dictionary = {i: bytes([i]) for i in range(dict_size)}
    result = bytearray()
    w = dictionary[compressed.pop(0)]
    result.extend(w)
    for k in compressed:
        if k in dictionary:
            entry = dictionary[k]
        elif k == dict_size:
            entry = w + w[:1]
        else:
            raise ValueError('Geçersiz sıkıştırılmış veri!')
        result.extend(entry)
        dictionary[dict_size] = w + entry[:1]
        dict_size += 1
        w = entry
    return bytes(result)

def compress_text_file(file_path, text_widget):
    output_path = filedialog.asksaveasfilename(defaultextension=".npy", filetypes=[("NumPy Files", "*.npy")])
    if not output_path:
        return
    original_size = os.path.getsize(file_path)
    with open(file_path, "rb") as f:
        text_bytes = f.read()
    compressed_data = compress_lzw(text_bytes)
    np.save(output_path, compressed_data)
    compressed_size = os.path.getsize(output_path)
    reduction = (1 - compressed_size / original_size) * 100
    messagebox.showinfo("Başarılı", f"Metin başarıyla sıkıştırıldı!\nOrijinal Boyut: {original_size} byte\nSıkıştırılmış Boyut: {compressed_size} byte\nAzalma: {reduction:.2f}%")

def decompress_text_file(compressed_file, text_widget):
    output_path = filedialog.asksaveasfilename(defaultextension=".txt", filetypes=[("Text Files", "*.txt")])
    if not output_path:
        return
    compressed_size = os.path.getsize(compressed_file)
    compressed_data = np.load(compressed_file, allow_pickle=True).tolist()
    decompressed_bytes = decompress_lzw(compressed_data)
    with open(output_path, "wb") as f:
        f.write(decompressed_bytes)
    decompressed_size = os.path.getsize(output_path)
    messagebox.showinfo("Başarılı", f"Metin başarıyla açıldı!\nSıkıştırılmış Boyut: {compressed_size} byte\nAçılmış Boyut: {decompressed_size} byte")

def open_text_file(text_widget):
    global text_file_path
    file_path = filedialog.askopenfilename(filetypes=[("Text Files", "*.txt")])
    if file_path:
        text_file_path = file_path
        with open(file_path, "r", encoding="utf-8") as f:
            text_widget.delete("1.0", tk.END)
            text_widget.insert(tk.END, f.read())

def open_compressed_file(text_widget):
    global compressed_file_path
    file_path = filedialog.askopenfilename(filetypes=[("NumPy Files", "*.npy")])
    if file_path:
        compressed_file_path = file_path

def start():
    gui = tk.Tk()
    gui.title("LZW Metin Sıkıştırma")
    gui['bg'] = 'lightblue'
    frame = tk.Frame(gui)
    frame.grid(row=0, column=0, padx=15, pady=15)
    frame['bg'] = 'gray'
    text_widget = tk.Text(frame, width=50, height=10)
    text_widget.grid(row=0, column=0, columnspan=3, padx=10, pady=10)
    btn1 = tk.Button(frame, text='Open Text', width=15, command=lambda: open_text_file(text_widget))
    btn1.grid(row=1, column=0)
    btn2 = tk.Button(frame, text='Compress', width=15, command=lambda: compress_text_file(text_file_path, text_widget))
    btn2.grid(row=1, column=1)
    btn3 = tk.Button(frame, text='Open Compressed', width=15, command=lambda: open_compressed_file(text_widget))
    btn3.grid(row=2, column=0)
    btn4 = tk.Button(frame, text='Decompress', width=15, command=lambda: decompress_text_file(compressed_file_path, text_widget))
    btn4.grid(row=2, column=1)
    gui.mainloop()

if __name__ == "__main__":
    start()
