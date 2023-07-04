export class MyFile implements component {

    private name: string;
    private type: string;
    private size: number;

    constructor(nome: string, tipo: string, tamanho: number) {
        this.setName(nome);
        this.setType(tipo);
        this.setSize(tamanho);
    }

    getSize(): number {
        return this.size;
    }
    getName(): string {
        return this.name;
    }
    getType(): string {
        return this.type;
    }
    addComponent(component: component): void {
        throw new Error("Cannot add files or subfolders to file");
    }
    removeComponent(component: String): void {
        throw new Error("Cannot remove files or subfolders to file");
    }
    getComponent(name: String): component {
        throw new Error("Cannot get files or subfolders to file");
    }

    setSize(size: number): void {
        this.size = size;
    }

    setName(name: string): void {
        this.name = name;
    }

    setType(type: string): void {
        this.type = type;
    }

}