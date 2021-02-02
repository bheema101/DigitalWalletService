import React, { Component } from "react";
import { FileDrop } from "react-file-drop";
import axios from "axios";
import "./Demo.css";
import Sheet from "react-modal-sheet";

class DragNDrop extends Component {
  state = {
    id: "",
    files: [],
    preview: "",
    downloadFileType: "",
    tuid: "",
    isOpen: false,
  };
  base_url = "http://localhost:8083/dws";
  componentDidMount() {
    //getAllFiles
    axios({
      method: "get",
      url: `${this.base_url}/getAllFiles/PN101/TU102`,
    })
      .then((response) => {
        const data = response.data[0];
        console.log(response);
        this.setState({ files: data.fileNames, id: data.id, tuid: data.tuid });
      })
      .catch((err) => console.log(err));
  }

  fileTypeChecker = (file) =>
    new Promise((resolve) => {
      const fileReader = new FileReader();

      fileReader.onloadend = (e) => {
        const arr = new Uint8Array(e.target.result).subarray(0, 4);
        let header = "";
        let type = null;
        for (let i = 0; i < arr.length; i++) {
          header += arr[i].toString(16);
        }
        console.log(header);
        switch (header) {
          case "89504e47": // png
          case "ffd8ffe0": // following are jpeg magic numbers
          case "ffd8ffe1":
          case "ffd8ffe2":
          case "ffd8ffe3":
          case "ffd8ffe8":
            type = "IMG";
            break;
          case "d0cf11e0": // xls
          case "504b34": // xlsx
            type = "XLS";
            break;
          case "25504446":
            type = "PDF";
            break;
          case "23206874":
          case "48656c6c":
          case "54686973":
            type = "TEXT";
            break;
          default:
            type = "unknown";
            break;
        }
        resolve(type);
      };

      fileReader.readAsArrayBuffer(file.slice(0, 4));
    });

  onDropFile = async (files, event) => {
    const fileType = await this.fileTypeChecker(files[0]);
    console.log(fileType);
    console.log(files[0]);
    if (fileType === "IMG" || fileType === "TEXT" || fileType === "PDF") {
      const formdata = new FormData();
      formdata.append("file", files[0]);
      formdata.append("tuid", "TU102");
      formdata.append("tripid", "TRIP101");
      formdata.append("pnr", "PN101");
      axios({
        method: "post",
        url: `${this.base_url}/uploadfilewithdata`,
        data: formdata,
        headers: { "Content-Type": "multipart/form-data" },
      })
        .then(() => {
          console.log("File uploaded successfully");
          axios({
            method: "get",
            url: `${this.base_url}/getAllFiles/PN101/TU102`,
          })
            .then((response) => {
              this.setState({ files: [...response.data[0].fileNames] });
            })
            .catch((err) => console.log(err));
        })
        .catch((err) => console.log(err));
    } else {
      alert("This file is not allowed!!!");
    }
  };

  onPreview(file, id, tuid) {
    console.log(file);

    axios({
      method: "post",
      url: `${this.base_url}/download`,
      data: { fileId: id, fileName: file, tuid: tuid },
      responseType: "blob",
    })
      .then((response) => {
        console.log(response);
        if (response.headers["content-type"] === "image/png" || response.headers["content-type"] === "image/jpg") {
          this.setState({
            preview: response.data,
            downloadFileType: response.headers["content-type"],
            isOpen:true
          });
        } else {
          const reader = new FileReader();

          reader.addEventListener("loadend", (e) => {
            const text = e.srcElement.result;
            this.setState({
              preview: text,
              downloadFileType: response.headers["content-type"],
              isOpen:true
            });
          });

          reader.readAsText(response.data);
        }
      })
      .catch((err) => console.log(err));
  }

  downloadFile(file, id, tuid) {
    console.log(file);
    const FileDownload = require("js-file-download");
    axios({
      method: "post",
      url: `${this.base_url}/download`,
      data: { fileId: id, fileName: file, tuid: tuid },
    })
      .then((response) => {
        console.log(response);
        FileDownload(response.data, file);
        console.log("File downloaded successfully");
      })
      .catch((err) => console.log(err));
  }

  setOpen(toggleCondition) {
    this.setState({ isOpen: toggleCondition });
  }

  render() {
    return (
      <div>
        <h1>Digital Wallet Demo</h1>
        <div>
          <FileDrop onDrop={(files, event) => this.onDropFile(files, event)}>
            {this.state.files.length > 0
              ? this.state.files.map((file) => (
                  <div key={file}>
                    <div
                      onClick={() =>
                        this.onPreview(file, this.state.id, this.state.tuid)
                      }
                      key={file}
                    >
                      {file}
                      <br />
                    </div>

                    <button
                      onClick={() =>
                        this.downloadFile(file, this.state.id, this.state.tuid)
                      }
                    >
                      Download
                    </button>
                  </div>
                ))
              : `Drop some files here!`}
            <br />
            <br />
            <br />


            <Sheet isOpen={this.state.isOpen} onClose={() => this.setOpen(false)}>
              <Sheet.Container>
                <Sheet.Header />
                <Sheet.Content>
                  <button
                    onClick={() => this.setOpen(false)}
                    style={{ float: "right" }}
                  >
                    X
                  </button>
                  <br />
                  {this.state.downloadFileType === "image/png" ||  this.state.downloadFileType === "image/jpg" ? (
                    <img
                      src={URL.createObjectURL(this.state.preview)}
                      // style={{ height: "900px", width: "900px" }}
                      style={{width:"90%"}}
                      alt="preview"
                    />
                  ) : (
                     <div>{this.state.preview}</div>
                    // window.open(URL.createObjectURL(this.state.preview))
                  )}
                </Sheet.Content>
              </Sheet.Container>

              <Sheet.Backdrop />
            </Sheet>
          </FileDrop>
        </div>
      </div>
    );
  }
}
export default DragNDrop;
