export class Folder implements component {

    private name: string;

    private components: component[];

    constructor(nome: string) {
        this.setName(nome);
        this.components = [];
    }

    getSize(): number {
        let size: number = 0;
        this.components.forEach(element => {
            size += element.getSize();
        }
        );
        return size;
    }

    getName(): string {
        return this.name;
    }
    getType(): string {
        return "folder";
    }
    addComponent(component: component): void {
        this.components.push(component);
    }
    removeComponent(componentName: String): void {
        this.components.forEach(element => {
            if (element.getName() == componentName) {
                this.components.splice(this.components.indexOf(element), 1);
            }
        }
        );
    }

    getComponent(name: String): component | null {
        let component: component | null = null;
        this.components.forEach(element => {
            if (element.getName() == name) {
                component = element;
            }
        }
        );
        return component;
    }

    setName(name: string): void {
        this.name = name;
    }
}