import urllib
import flask, socket, base64, pathlib, os, subprocess, time
from urllib import parse
from PIL import Image
import io
from flask.templating import render_template

app = flask.Flask(__name__, static_url_path="/static")
app.config["DEBUG"] = True

isBodySeen: bool = False

@app.route('/', methods=['GET', 'POST'])
def home():
    data = flask.request.get_json().get("data")
    print(data)
    im = Image.open(io.BytesIO(base64.urlsafe_b64decode(data)))
    im.save("static/img.jpg")
    return render_template('camera.html', img_base64="/static/img.jpg")

@app.route('/preview', methods=['GET'])
def preview():
    return render_template('camera.html', img_base64="static/img.jpg")

@app.route('/entry', methods=['GET', 'POST'])
def entry():
    data = flask.request.args.get("status", default=None, type=None)
    global isBodySeen
    if data == "false":
        isBodySeen = False
    elif data == "true":
        isBodySeen = True
    return("done.")

@app.route('/entrystatus', methods=['GET'])
def entrystatus():
    print(isBodySeen)
    return render_template('entry.html', status=isBodySeen)

app.run(host="0.0.0.0", port=5000)
