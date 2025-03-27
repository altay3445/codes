from PIL import Image, ImageTk
import numpy as np
import os
import tkinter as tk
from tkinter import filedialog, messagebox

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
            raise ValueError('Geçersiz veri!')
        result.extend(entry)
        dictionary[dict_size] = w + entry[:1]
        dict_size += 1
        w = entry
    return bytes(result)

def image_to_bytes(image_path, mode='L'):
    img = Image.open(image_path)
    img = img.convert(mode)
    img_bytes = np.array(img, dtype=np.uint8).tobytes()
    return img_bytes, img.size

def bytes_to_image(byte_data, size):
    img_array = np.frombuffer(byte_data, dtype=np.uint8).reshape(size[::-1])
    return Image.fromarray(img_array)

def compress_image(image_path, image_panel):
    output_path = filedialog.asksaveasfilename(defaultextension=".npy", filetypes=[("NumPy Files", "*.npy")])
    if not output_path:
        return
    original_size = os.path.getsize(image_path)
    img_bytes, size = image_to_bytes(image_path)
    compressed_data = compress_lzw(img_bytes)
    np.save(output_path, compressed_data)
    compressed_size = os.path.getsize(output_path)
    messagebox.showinfo("Başarılı", f"Görüntü başarıyla sıkıştırıldı!\nOrijinal Boyut: {original_size} byte\nSıkıştırılmış Boyut: {compressed_size} byte")
    return compressed_data, size

def decompress_current_image(image_panel, compressed_data, size):
    decompressed_bytes = decompress_lzw(compressed_data)
    img = bytes_to_image(decompressed_bytes, size)
    img = ImageTk.PhotoImage(img)
    image_panel.config(image=img)
    image_panel.photo_ref = img
    messagebox.showinfo("Decompress", f"Görüntü başarıyla açıldı!\nBoyut: {size[0]}x{size[1]}")

def open_image(image_panel):
    global image_file_path
    file_path = filedialog.askopenfilename(filetypes=[("BMP Image", "*.bmp"), ("PNG Image", "*.png")])
    if file_path:
        image_file_path = file_path
        img = Image.open(image_file_path)
        img = ImageTk.PhotoImage(img)
        image_panel.config(image=img)
        image_panel.photo_ref = img

def start():
    gui = tk.Tk()
    gui.title("LZW Görüntü Sıkıştırma")
    gui['bg'] = 'SeaGreen1'
    frame = tk.Frame(gui)
    frame.grid(row=0, column=0, padx=15, pady=15)
    frame['bg'] = 'DodgerBlue4'
    gui_img_panel = tk.Label(frame)
    gui_img_panel.grid(row=0, column=0, columnspan=3, padx=10, pady=10)
    btn1 = tk.Button(frame, text='Open Image', width=10, command=lambda: open_image(gui_img_panel))
    btn1.grid(row=1, column=0)
    btn2 = tk.Button(frame, text='Compress', width=10, command=lambda: compress_image(image_file_path, gui_img_panel))
    btn2.grid(row=1, column=1)
    btn3 = tk.Button(frame, text='Decompress', width=10, command=lambda: decompress_current_image(gui_img_panel, *compress_image(image_file_path, gui_img_panel)))
    btn3.grid(row=1, column=2)
    gui.mainloop()

if __name__ == "__main__":
    start()
