import { Folder } from "./model/Folder";
import { MyFile } from "./model/MyFile";

function main() {

    let root: Folder = new Folder("root");
    root.addComponent(new Folder("images"));
    root.addComponent(new Folder("documents"));
    root.getComponent("images")?.addComponent(new MyFile("image1", "png", 100));
    root.getComponent("images")?.addComponent(new Folder("photos"));
    root.getComponent("images")?.getComponent("photos")?.addComponent(new MyFile("image2", "png", 200));

    let videos = root.getComponent("videos");
    console.log("Folder videos: " + videos);

    console.log(root.getComponent("images"));
    console.log(root.getComponent("image2"));

    console.log("root folder size: " + root.getSize());

    root.getComponent("images")?.removeComponent("photos");
    console.log("removed photo folder \nroot folder size: " + root.getSize());
    console.log(root.getComponent("images"));

    console.log("file : " + root.getComponent("images")?.getComponent("image1")?.getName());
    console.log("type : " + root.getComponent("images")?.getComponent("image1")?.getType());
    console.log("size : " + root.getComponent("images")?.getComponent("image1")?.getSize());

    root.getComponent("images")?.removeComponent("image1");

    console.log("removed image1 file... \nroot folder size: " + root.getSize());
    console.log("documents folder size: " + root.getComponent("documents")?.getSize());
    console.log("Images folder size: " + root.getComponent("images")?.getSize());
}

main();